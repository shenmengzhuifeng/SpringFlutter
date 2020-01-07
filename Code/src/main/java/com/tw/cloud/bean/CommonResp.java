package com.tw.cloud.bean;


import com.tw.cloud.utils.Constants;
import com.tw.cloud.utils.ResultCode;

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

    /**
     * 成功返回结果
     *
     * @param data 获取的数据
     */
    public static <T> CommonResp<T> success(T data) {
        return new CommonResp<T>(Constants.RESULT_OK,"","", data);
    }

    /**
     * 成功返回结果
     *
     * @param data 获取的数据
     * @param  message 提示信息
     */
    public static <T> CommonResp<T> success(T data, String message) {
        return new CommonResp<T>(Constants.RESULT_OK,"", message, data);
    }

    /**
     * 失败返回结果
     * @param errorCode 错误码
     */
    public static <T> CommonResp<T> failed(ResultCode errorCode) {
        return new CommonResp<T>(Constants.RESULT_OK,errorCode.getCode(), errorCode.getMessage(), null);
    }

    /**
     * 失败返回结果
     * @param message 提示信息
     */
    public static <T> CommonResp<T> failed(String message) {
        return new CommonResp<T>(Constants.RESULT_ERROR,ResultCode.RESULT_CODE_1002.getCode(), message, null);
    }

    /**
     * 失败返回结果
     */
    public static <T> CommonResp<T> failed() {
        return failed(ResultCode.RESULT_CODE_1002);
    }

    /**
     * 参数验证失败返回结果
     */
    public static <T> CommonResp<T> validateFailed() {
        return failed(ResultCode.RESULT_CODE_1003);
    }

    /**
     * 参数验证失败返回结果
     * @param message 提示信息
     */
    public static <T> CommonResp<T> validateFailed(String message) {
        return new CommonResp<T>(Constants.RESULT_ERROR,ResultCode.RESULT_CODE_1003.getCode(), message, null);
    }

    /**
     * 未登录返回结果
     */
    public static <T> CommonResp<T> unauthorized(T data) {
        return new CommonResp<T>(Constants.RESULT_ERROR,ResultCode.RESULT_CODE_1004.getCode(), ResultCode.RESULT_CODE_1004.getMessage(), data);
    }

    /**
     * 未授权返回结果
     */
    public static <T> CommonResp<T> forbidden(T data) {
        return new CommonResp<T>(Constants.RESULT_ERROR,ResultCode.RESULT_CODE_1005.getCode(), ResultCode.RESULT_CODE_1005.getMessage(), data);
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
