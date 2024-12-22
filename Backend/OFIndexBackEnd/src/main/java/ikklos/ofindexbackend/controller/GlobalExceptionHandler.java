package ikklos.ofindexbackend.controller;

import ikklos.ofindexbackend.utils.UniversalBadReqException;
import ikklos.ofindexbackend.utils.UniversalResponse;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UniversalBadReqException.class)
    public ResponseEntity<UniversalResponse> handleUniversalBadReqException(UniversalBadReqException ex){
        UniversalResponse response=new UniversalResponse();
        response.message="Bad request:"+ex.getMessage();
        return ResponseEntity.status(ex.getHttpCode()).body(response);
    }

    @ExceptionHandler(value={SignatureException.class, MalformedJwtException.class})
    public ResponseEntity<UniversalResponse> handleSignatureException(Exception ex){
        UniversalResponse response=new UniversalResponse();
        response.message="Token failed! : "+ex.getClass()+" : "+ex.getMessage();
        return ResponseEntity.status(601).body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<UniversalResponse> handleRuntimeException(RuntimeException ex) {
        UniversalResponse response=new UniversalResponse();
        response.message=ex.getClass()+" : "+ex.getMessage();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<UniversalResponse> handleGenericException(Exception ex) {
        UniversalResponse response=new UniversalResponse();
        response.message="Error: " + ex.getMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

}