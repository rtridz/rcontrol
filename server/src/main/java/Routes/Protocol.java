package Routes;

/**
 * Created by vadim on 27.09.15.
 */
public class Protocol {
    private int code;
    private String message;
    private Object response;

    public Protocol(int code, String message, Object response) {
        this.code = code;
        this.message = message;
        this.response = response;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Object getResponse() {
        return response;
    }
}
