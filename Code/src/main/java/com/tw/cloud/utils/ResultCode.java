package com.tw.cloud.utils;

/**
 *
 */
public enum ResultCode implements IErrorCode {

    RESULT_CODE_1001(ErrorCode.ERROR_CODE_1001, ""),
    RESULT_CODE_1002(ErrorCode.ERROR_CODE_1002, "操作失败"),
    RESULT_CODE_1003(ErrorCode.ERROR_CODE_1003, "参数检验失败"),
    RESULT_CODE_1004(ErrorCode.ERROR_CODE_1004, "暂未登录或token已经过期"),
    RESULT_CODE_1005(ErrorCode.ERROR_CODE_1005, "没有相关权限");

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
