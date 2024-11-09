package com.unihack.smart_usb.api.error_handling;


import com.unihack.smart_usb.exception.auth.EntityDoesNotExistException;
import com.unihack.smart_usb.exception.auth.ProfessorAlreadyExistsException;
import com.unihack.smart_usb.exception.auth.UserDoesNotOwnEntityException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MainExceptionHandler {

    @ExceptionHandler(ProfessorAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MainErrorResponse handlerProfessorAlreadyExistsException(ProfessorAlreadyExistsException ex) {
        return MainErrorResponse.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .reasonPhrase(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(ex.getMessage())
                .code("PROFESSOR_EXISTS")
                .build();
    }

    @ExceptionHandler(UserDoesNotOwnEntityException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MainErrorResponse handlerUserDoesNotOwnEntityException(UserDoesNotOwnEntityException ex) {
        return MainErrorResponse.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .reasonPhrase(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(ex.getMessage())
                .code("OWNERSHIP_VIOLATION")
                .build();
    }

    @ExceptionHandler(EntityDoesNotExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MainErrorResponse handlerEntityDoesNotExistException(EntityDoesNotExistException ex) {
        return MainErrorResponse.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .reasonPhrase(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(ex.getMessage())
                .code("ENTITY_DOES_NOT_EXIST")
                .build();
    }
}
