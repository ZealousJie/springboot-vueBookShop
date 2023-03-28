package com.example.demo.common.exception;

/**
 * @ author zealousJie
 * @ version 1.0
 */
public class CustomException2 extends RuntimeException{

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
    public CustomException2(String message){
        super(message);
        this.errorMessage= message;
    }

    public CustomException2(String message,int code){
        super(message);
        this.errorMessage = message;
        this.errorCode = code;
    }

    public CustomException2(String message,Throwable cause){
        super(message,cause);
        this.errorMessage= message;
    }

    public CustomException2(String message,int code,Throwable cause){
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
