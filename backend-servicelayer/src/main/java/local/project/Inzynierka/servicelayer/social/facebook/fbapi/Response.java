package local.project.Inzynierka.servicelayer.social.facebook.fbapi;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Response<T> {

    private T data;

    @JsonCreator
    public Response(@JsonProperty("data") T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }
}
