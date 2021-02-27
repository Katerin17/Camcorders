import camcorders.Camera;
import camcorders.CameraDataAggregator;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

@SuppressWarnings("checkstyle:LineLength")
public class CameraDataAggregatorTest {

    @Test
    public void dataShouldBeMatch() throws InterruptedException, ExecutionException, JsonProcessingException {
        Camera expected = new Camera(1, "LIVE", "rtsp://127.0.0.1/1", "fa4b588e-249b-11e9-ab14-d663bd873d93", 120);
        expected.setTokenDataUrl("http://www.mocky.io/v2/5c51b5b6340000554e129f7b?mocky-delay=1s");
        expected.setSourceDataUrl("http://www.mocky.io/v2/5c51b230340000094f129f5d");
        CameraDataAggregator cameraDataAggregator = new CameraDataAggregator();
        Camera result = cameraDataAggregator.getResultArrayOfCameras("http://www.mocky.io/v2/5c51b9dd3400003252129fb5")[0];
        Assert.assertEquals(expected, result);
    }

    @Test
    public void fieldsShouldNotBeNull() throws InterruptedException, ExecutionException, JsonProcessingException {
        Camera cam = new Camera();
        cam.setId(20);
        cam.setTokenDataUrl("http://www.mocky.io/v2/5c51b5ed340000554e129f7e");
        cam.setSourceDataUrl("http://www.mocky.io/v2/5c51b2e6340000a24a129f5f?mocky-delay=100ms");
        CameraDataAggregator cameraDataAggregator = new CameraDataAggregator();
        Camera result = cameraDataAggregator.getResultArrayOfCameras("http://www.mocky.io/v2/5c51b9dd3400003252129fb5")[1];
        Assert.assertNotNull(result.getUrlType());
        Assert.assertNotNull(result.getValue());
        Assert.assertNotNull(result.getVideoUrl());
        Assert.assertNotEquals(0, result.getTtl());
    }

    @Test
    public void arrayLengthShouldBeFour() throws ExecutionException, InterruptedException, JsonProcessingException {
        CameraDataAggregator cameraDataAggregator = new CameraDataAggregator();
        Camera[] cams = cameraDataAggregator.getResultArrayOfCameras("http://www.mocky.io/v2/5c51b9dd3400003252129fb5");
        Assert.assertEquals(cams.length, 4);
    }
}
