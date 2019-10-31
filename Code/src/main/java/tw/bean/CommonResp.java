package tw.bean;



/**
 * 公共的接口返回bean
 *
 * @param <T>
 */
public class CommonResp<T> {


    private int resultCode;

    private String errorCode;

    private String errorMsg;

    private T response;

    public CommonResp(int resultCode, String errorCode, String errorMsg, T response) {
        this.resultCode = resultCode;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.response = response;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public T getResponse() {
        return response;
    }

    public void setResponse(T response) {
        this.response = response;
    }
}
