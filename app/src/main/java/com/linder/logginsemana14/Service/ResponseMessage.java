package com.linder.logginsemana14.Service;

/**
 * Created by linderhassinger on 11/13/17.
 */

public class ResponseMessage {
    private String type;

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ResponseMessage{" +
                "type='" + type + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
