package cn.tycoding.chatfriend.server.exception;

import cn.tycoding.chatfriend.common.core.api.R;
import cn.tycoding.chatfriend.common.core.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R<String> handleServiceException(ServiceException e) {
        log.error("Service Exception: {}", e.getMessage());
        return R.fail(e.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public R<String> handleNotFoundException(ResourceNotFoundException e) {
        log.error("Resource Not Found: {}", e.getMessage());
        return R.fail(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R<String> handleArgumentMismatch(MethodArgumentTypeMismatchException e) {
        log.error("Argument Mismatch: {}", e.getMessage());
        return R.fail("Invalid parameter: " + e.getName());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public R<String> handleException(Exception e) {
        log.error("System Exception: ", e);
        return R.fail("System error occurred. Please contact admin.");
    }
}
