package camcorders;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * Class is model of object Camera.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Camera {

    /**
     * Identifier of camera.
     */
    private int id;

    /**
     * Object data reference (fields: tokenDataUrl and videoUrl)
     *
     * This field is auxiliary data and is not serialized to the object.
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String sourceDataUrl;

    /**
     * Object data reference (fields: value and ttl)
     *
     * This field is auxiliary data and is not serialized to the object.
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String tokenDataUrl;

    /**
     * Type of link to the video stream. Possible values - "LIVE"/"ARCHIVE".
     */
    private String urlType;

    /**
     * Link to the video stream.
     */
    private String videoUrl;

    /**
     * Security token.
     */
    private String value;

    /**
     * Token lifetime.
     */
    private int ttl;

    /**
     * Creates a Camera.
     */
    public Camera() {
    }

    /**
     * Constructs instance of Camera that uses specified fields -
     * identifier, type of link to the video stream, link to the video stream,
     * security token, token lifetime.
     */
    public Camera(int id, String urlType, String videoUrl, String value, int ttl) {
        this.id = id;
        this.urlType = urlType;
        this.videoUrl = videoUrl;
        this.value = value;
        this.ttl = ttl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSourceDataUrl() {
        return sourceDataUrl;
    }

    public void setSourceDataUrl(String sourceDataUrl) {
        this.sourceDataUrl = sourceDataUrl;
    }

    public String getTokenDataUrl() {
        return tokenDataUrl;
    }

    public void setTokenDataUrl(String tokenDataUrl) {
        this.tokenDataUrl = tokenDataUrl;
    }

    public String getUrlType() {
        return urlType;
    }

    public void setUrlType(String urlType) {
        this.urlType = urlType;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getTtl() {
        return ttl;
    }

    public void setTtl(int ttl) {
        this.ttl = ttl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Camera camera = (Camera) o;
        return id == camera.id &&
                ttl == camera.ttl &&
                Objects.equals(urlType, camera.urlType) &&
                Objects.equals(videoUrl, camera.videoUrl) &&
                Objects.equals(value, camera.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, urlType, videoUrl, value, ttl);
    }
}
