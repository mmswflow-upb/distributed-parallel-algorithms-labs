package imagestoreq;

public class Request {
    public int terminalId;
    public String function;   // "post", "list", "get"
    public Object args;       // depends on function

    public Request(int terminalId, String function, Object args) {
        this.terminalId = terminalId;
        this.function = function;
        this.args = args;
    }
}
