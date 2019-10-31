package tw.utils;

/**
 *
 */
public enum ResultCode implements IErrorCode {

    RESULT_CODE_1001(ErrorCode.ERROR_CODE_1001, "");


    private String code;
    private String message;

    private ResultCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
