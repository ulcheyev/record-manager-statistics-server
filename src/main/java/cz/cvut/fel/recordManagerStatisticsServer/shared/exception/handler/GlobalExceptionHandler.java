package cz.cvut.fel.recordManagerStatisticsServer.shared.exception.handler;

import cz.cvut.fel.recordManagerStatisticsServer.dto.ErrorResponse;
import cz.cvut.fel.recordManagerStatisticsServer.shared.exception.StatisticsException;
import cz.cvut.fel.recordManagerStatisticsServer.shared.exception.StatisticsNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(StatisticsNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(
            StatisticsNotFoundException ex,
            HttpServletRequest request
    ) {
        log.warn("Not found: {}", ex.getMessage());
        return build(HttpStatus.NOT_FOUND, ex, request);
    }

    @ExceptionHandler(StatisticsException.class)
    public ResponseEntity<ErrorResponse> handleGeneral(
            StatisticsException ex,
            HttpServletRequest request
    ) {
        log.error("Statistics error: {}", ex.getMessage(), ex);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, ex, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(
            Exception ex,
            HttpServletRequest request
    ) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, ex, request);
    }

    private ResponseEntity<ErrorResponse> build(
            HttpStatus status,
            Exception ex,
            HttpServletRequest request
    ) {
        return ResponseEntity.status(status).body(
                ErrorResponse.builder()
                        .status(status.value())
                        .error(status.getReasonPhrase())
                        .message(ex.getMessage())
                        .path(request.getRequestURI())
                        .timestamp(Instant.now())
                        .build()
        );
    }
}