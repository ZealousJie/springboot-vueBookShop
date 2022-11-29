package com.example.demo.common.exception;

/**
 * 自定义异常
 * @ author zealousJie
 * @ version 1.0
 */
public class CustomException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    /**
     * 错误的代码
     */
    private int errorCode;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 不同的构造方法
     * @param message
     */
    public CustomException(String message){
        super(message);
        this.errorMessage= message;
    }

    public CustomException(String message,int code){
        super(message);
        this.errorMessage = message;
        this.errorCode = code;
    }

    public CustomException(String message,Throwable cause){
        super(message,cause);
        this.errorMessage= message;
    }

    public CustomException(String message,int code,Throwable cause){
        super(message,cause);
        this.errorMessage= message;
        this.errorCode = code;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
