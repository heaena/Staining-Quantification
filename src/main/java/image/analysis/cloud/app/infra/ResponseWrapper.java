package image.analysis.cloud.app.infra;


public class ResponseWrapper<T> {
    public static final int CODE_SUCCESS = 0;
    public static final int CODE_FAIL = -1;
    public static final String MSG_SUCCESS = "success";
    public static final String MSG_FAIL = "fail";
    private int code;
    private String msg;
    private T data;

    public ResponseWrapper(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResponseWrapper(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> ResponseWrapper<T> success(T data) {
        return new ResponseWrapper(CODE_SUCCESS, MSG_SUCCESS, data);
    }

    public static ResponseWrapper success() {
        return new ResponseWrapper(CODE_SUCCESS, MSG_SUCCESS);
    }

    public static ResponseWrapper fail() {
        return new ResponseWrapper(CODE_FAIL, MSG_FAIL);
    }

    public static ResponseWrapper fail(String msg) {
        return new ResponseWrapper(CODE_FAIL, msg);
    }

    public boolean isSuccess() {
        return code == CODE_SUCCESS;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
