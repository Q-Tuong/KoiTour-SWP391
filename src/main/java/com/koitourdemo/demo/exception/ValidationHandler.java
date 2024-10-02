package com.koitourdemo.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ValidationHandler {// đánh dấu đây là 1 class để bắt exception
    // MethodArgumentException : thư viện quăng ra lỗi này nếu invalid argument
    //đánh dấu hàm này sẽ chạy mỗi khi chương trình gặp lỗi này
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleValidation (MethodArgumentNotValidException exception){
        String msg = "";
        for(FieldError fieldError :exception.getBindingResult().getFieldErrors()){
            //loop qua từng field của dữ liệu , nếu cái nào có lỗi thì thêm vào msg
            msg += fieldError.getDefaultMessage()+"\n";

        }
        return new ResponseEntity(msg, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleValidation (Exception exception){
        // mỗi khi gặp lỗi này lập tức gọi
        return new ResponseEntity(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
