package imagestoreq;

public class Response {
    public String function;
    public Object data;

    public Response(String function, Object data) {
        this.function = function;
        this.data = data;
    }
}
