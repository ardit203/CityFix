package finki.ukim.backend.common.handler;

import finki.ukim.backend.common.dto.ApiError;
import finki.ukim.backend.common.exception.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiError> handleConflict(ConflictException exception) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiError.of(HttpStatus.CONFLICT, exception.getMessage()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFound(ResourceNotFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiError.of(HttpStatus.NOT_FOUND, exception.getMessage()));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> handleBadRequest(BadRequestException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiError.of(HttpStatus.BAD_REQUEST, exception.getMessage()));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiError> handleUnauthorized(UnauthorizedException exception) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiError.of(HttpStatus.UNAUTHORIZED, exception.getMessage()));
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ApiError> handleForbidden(ForbiddenException exception) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ApiError.of(HttpStatus.FORBIDDEN, exception.getMessage()));
    }

    /// /////////////

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiError.of(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiError.of(HttpStatus.BAD_REQUEST, "Validation failed"));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiError> handleMissingParameter(MissingServletRequestParameterException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiError.of(
                        HttpStatus.BAD_REQUEST,
                        "Missing required parameter: " + exception.getParameterName()
                ));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleUnreadableBody(HttpMessageNotReadableException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiError.of(HttpStatus.BAD_REQUEST, "Request body is missing or invalid"));
    }

//    @ExceptionHandler(DataIntegrityViolationException.class)
//    public ResponseEntity<ApiError> handleDataIntegrity(DataIntegrityViolationException exception) {
//        return ResponseEntity
//                .status(HttpStatus.CONFLICT)
//                .body(ApiError.of(HttpStatus.CONFLICT, exception.getMessage()));
//    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDenied(AccessDeniedException exception) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ApiError.of(HttpStatus.FORBIDDEN, "Access denied"));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleTypeMismatch(MethodArgumentTypeMismatchException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiError.of(HttpStatus.BAD_REQUEST, "Invalid parameter type"));
    }
}