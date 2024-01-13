package kr.project.backend.handler;

import jakarta.servlet.http.HttpServletRequest;
import kr.project.backend.common.ResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice

@Order(Ordered.LOWEST_PRECEDENCE)
public class GlobalExceptionHandler {

    /*@ExceptionHandler(Exception.class)
    public org.springframework.http.ResponseEntity<ResponseEntity<Void>> exceptionHandler(HttpServletRequest httpServletRequest, Exception e){

        ResponseEntity<Void> r = new ResponseEntity<>();
        r.setCode("9999");
        r.setMsg("server error 발생");

        return new org.springframework.http.ResponseEntity<>(r, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public org.springframework.http.ResponseEntity<ResponseEntity<Void>> bindExceptionHandler(HttpServletRequest httpServletRequest, MethodArgumentNotValidException e){

        ResponseEntity<Void> r = new ResponseEntity<>();
        r.setCode("9998");
        //r.setMsg("parameter 누락 ( "+e.getBindingResult().getFieldErrors().getFirst().getField()+" )");
        r.setMsg("parameter 누락 ");

        return new org.springframework.http.ResponseEntity<>(r, HttpStatus.BAD_REQUEST);
    }*/

}
