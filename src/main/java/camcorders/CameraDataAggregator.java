package camcorders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.*;

@SuppressWarnings("checkstyle:LineLength")
public class CameraDataAggregator {

    /**
     * Initializes the number of available processors
     */
    private final int numberOfCores = Runtime.getRuntime().availableProcessors();

    /**
     * Initializes {@link ObjectMapper}
     */
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Initializes thread pool for execute aggregation
     */
    private final ExecutorService executorCam = Executors.newFixedThreadPool(numberOfCores);

    /**
     * Initializes {@link HttpClient} with own {@link ExecutorService}
     */
    private final HttpClient httpClient = HttpClient.newBuilder()
            .executor(Executors.newFixedThreadPool(numberOfCores))
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    /**
     * Gets the body JSON object by the passed URI link using {@link HttpClient}.
     * <p>
     * A method uses class {@link CompletableFuture<>} to get the query result
     * asynchronously.
     *
     * @param url The string to be parsed into a URI
     * @return body of JSON object {@link Camera} or JSONArray
     * @throws ExecutionException   thrown in the {@code result.get()}
     *                              if this future completed exceptionally.
     * @throws InterruptedException thrown in the {@code result.get()}
     *                              if the current thread was interrupted while waiting.
     */
    private String getResponse(String url) throws ExecutionException, InterruptedException {
        String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36";
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .setHeader("User-Agent", userAgent)
                .build();
        CompletableFuture<HttpResponse<String>> result = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        return result.get().body();
    }

    /**
     * Gets JSON string resulting from the aggregation of objects
     * by the passed URI link.
     * <p>
     * A method uses class {@link FutureTask<>} to get the query result
     * asynchronously.
     *
     * @param url The string to be parsed into a URI
     * @return string of JSON object {@link Camera} or JSONArray with all
     * the collected data for each object
     * @throws ExecutionException      thrown in the {@code result.get()} or
     *                                 {@code getResponse()} if this future completed exceptionally.
     * @throws InterruptedException    thrown in the {@code result.get()} or
     *                                 {@code getResponse()} if the current thread was interrupted while waiting.
     * @throws JsonProcessingException thrown in the {@code readValue()}
     *                                 for all problems encountered when processing JSON content
     *                                 that are not pure I/O problems
     */
    public Camera[] getResultArrayOfCameras(String url) throws ExecutionException, InterruptedException, JsonProcessingException {
        String jsonCam = getResponse(url);
        Camera[] arr = mapper.readValue(jsonCam, Camera[].class);
        for (Camera camera : arr) {
            FutureTask<Camera> cam = new FutureTask<>(new Aggregator(camera));
            executorCam.execute(cam);
            cam.get();
        }
        return arr;
    }

    /**
     * Converts an array of objects {@link Camera} to JSON String.
     * <p>
     *
     * @param cams array of object
     * @return string in JSON format (JSONArray)
     * @throws JsonProcessingException thrown in the {@code writeValueAsString()}
     *                                 for all problems encountered when processing JSON content
     *                                 that are not pure I/O problems
     */
    public String arrayToJson(Camera[] cams) throws JsonProcessingException {
        String result = mapper.writeValueAsString(cams);
        return result.replaceAll(",", ",\n");
    }

    /**
     * Inner class to aggregate information on objects using separate threads.
     * <p>
     * Class implements interface {@link Callable<Camera>}
     * Overridden method {@code call()} gets additional data on the links
     * (fields of object: SourceDataUrl and TokenDataUrl) specified in the object
     * and returns an object {@link Camera} filled with all available data.
     */
    private class Aggregator implements Callable<Camera> {

        /**
         * Object {@link Camera} for data aggregation
         */
        private final Camera camera;

        /**
         * Constructs instance of Aggregator that uses specified {@link Camera}.
         */
        public Aggregator(Camera camera) {
            this.camera = camera;
        }

        @Override
        public Camera call() throws ExecutionException, InterruptedException, JsonProcessingException {
            JsonNode nodeA = mapper.readTree(getResponse(camera.getSourceDataUrl()));
            camera.setUrlType(nodeA.path("urlType").asText());
            camera.setVideoUrl(nodeA.path("videoUrl").asText());
            JsonNode nodeB = mapper.readTree(getResponse(camera.getTokenDataUrl()));
            camera.setValue(nodeB.path("value").asText());
            camera.setTtl(nodeB.path("ttl").asInt());
            return camera;
        }
    }

    /**
     * Console output test method
     */
    public static void main(String[] args) {
        CameraDataAggregator cameraDataAggregator = new CameraDataAggregator();
        try {
            Camera[] arr = cameraDataAggregator.getResultArrayOfCameras("http://www.mocky.io/v2/5c51b9dd3400003252129fb5");
            System.out.println(cameraDataAggregator.arrayToJson(arr));
        } catch (ExecutionException | InterruptedException | JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}

