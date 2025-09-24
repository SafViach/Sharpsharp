package com.sharp.sharpshap.exceptions;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.sharp.sharpshap.error.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(UserException.class)
    public ResponseEntity<String> handlerUserNotFound(UserException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(TradePointNotFoundException.class)
    public ResponseEntity<String> handlerTradePointNotFound(TradePointNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<String> handlerCategoryNotFound(CategoryNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handlerGeneralException(Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ошибка сервера :" + exception.getMessage());
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<String> handlerRoleException(RoleNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<String> handleNotFound(NoHandlerFoundException exception) {
        if (exception.getRequestURL().contains(".well-known/appspecific/com.chrome.devtools.json.")) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Страница не найдена");
    }

    @ExceptionHandler(UserNotAuthenticated.class)
    public ResponseEntity<String> handlerNotAuthenticated(UserNotAuthenticated exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(RefreshTokenNotFound.class)
    public ResponseEntity<String> handlerNotAuthenticated(RefreshTokenNotFound exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handlerValidationError(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> passwordIncorrect(BadCredentialsException exception){
        logger.error("GlobalExceptionHandler: passwordIncorrect(BadCredentialsException)  ~~~Неверный пароль");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Неверный пароль");
    }
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> userNotFound(UsernameNotFoundException exception){
        logger.error("GlobalExceptionHandler: ---UsernameNotFoundException Неверный логин" );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }
    private ResponseEntity<String> error(Exception e, HttpStatus status) {
        ErrorResponse error = new ErrorResponse(
                e.getMessage(),
                status.value(),
                LocalDateTime.now().toString()
        );
        return ResponseEntity.status(status).body(error.getMessage());
    }
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<String>handlerResourceAlreadyExistsException(ResourceAlreadyExistsException exception){
        logger.error("GlobalExceptionHandler: handlerResourceAlreadyExistsException(ResourceAlreadyExistsException)  ~~~Уже есть в базе");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }
    @ExceptionHandler(DeleteException.class)
    public ResponseEntity<String>errorDelete(DeleteException exception){
        logger.error("GlobalExceptionHandler: errorDelete(DeleteException  ~~Отмена удаления" + exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }
    @ExceptionHandler(CreateProductException.class)
    public ResponseEntity<String>errorCreateProduct(CreateProductException exception){
        logger.error("GlobalExceptionHandler: errorCreateProduct(CreateProductException  ~~Отмена создания продукта"
                + exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        Throwable cause = ex.getCause();

        if (cause instanceof InvalidFormatException ife) {
            Class<?> targetType = ife.getTargetType();

            if (targetType != null && targetType == java.math.BigDecimal.class) {
                return ResponseEntity.badRequest().body(String.format(
                        "{\"error\": \"Значение "+ ife.getValue() +" не является допустимым числом. Ожидалось число \"}"
                ));
            }
        }

        return ResponseEntity.badRequest().body(
                "{\"error\": \"Некорректный формат данных. Проверьте, что вводите число.\"}"
        );
    }
    @ExceptionHandler(ProductChangeRequestNotFoundException.class)
    public ResponseEntity<String>errorFound(ProductChangeRequestNotFoundException exception){
        logger.error("ProductChangeRequestNotFoundException: errorFound(DeleteException  ~~нет в БД" + exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }
}
