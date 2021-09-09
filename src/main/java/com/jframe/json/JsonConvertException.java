package com.jframe.json;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * json 异常
 * @author jiangjian45
 * @date 2021/9/8 15:33
 */
public class JsonConvertException extends RuntimeException{

    private Exception processingException;


    public JsonConvertException(Exception processingException) {
        this.processingException = processingException;
    }


    public JsonConvertException(String message) {
        super(message);
    }

    @Override
    public void printStackTrace() {
        processingException.printStackTrace();
    }

    @Override
    public String toString() {
        return processingException.toString();
    }

    @Override
    public String getMessage() {
        return processingException.getMessage();
    }

    @Override
    public String getLocalizedMessage() {
        return processingException.getLocalizedMessage();
    }

    @Override
    public synchronized Throwable getCause() {
        return processingException.getCause();
    }

    @Override
    public synchronized Throwable initCause(Throwable cause) {
        return processingException.initCause(cause);
    }

    @Override
    public void printStackTrace(PrintStream s) {
        processingException.printStackTrace(s);
    }

    @Override
    public void printStackTrace(PrintWriter s) {
        processingException.printStackTrace(s);
    }
}
