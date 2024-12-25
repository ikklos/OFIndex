package ikklos.ofindexbackend.utils;

import lombok.Getter;

public class UniversalBadReqException extends Exception{

    @Getter
    private final int httpCode;
    private final String message;

    public UniversalBadReqException(String message){
        this(502,message);
    }

    public UniversalBadReqException(int httpCode,String message){
        this.httpCode=httpCode;
        this.message=message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
