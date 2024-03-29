package demo.config;

import org.springframework.beans.TypeMismatchException;
import org.springframework.boot.context.properties.bind.BindException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.lang.NonNull;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ExceptionRest {

    public static void throwAnyIF(org.slf4j.Logger logger, Throwable throwable, HttpStatusCode status, String msg) {
        if (logger != null)
            logger.error("{} - {}", status, msg);
        if (throwable == null) {
            throw new ApiException(status, msg);
        } else {
            throw new ApiException(status, throwable, msg);
        }
    }

    public static void throwAnyIF(HttpStatusCode status, String msg) {
        throwAnyIF(null, null, status, msg);
    }

    public static void throwAnyIF(org.slf4j.Logger logger, HttpStatusCode status, String msg) {
        throwAnyIF(logger, null, status, msg);
    }

    public static void throwBadRequestIF(org.slf4j.Logger logger, String msg, boolean condition) {
        if (condition)
            throwAnyIF(logger, HttpStatus.BAD_REQUEST, msg);
    }

    public static void throwBadRequestIF(String msg, boolean condition) {
        throwBadRequestIF(null, msg, condition);
    }

    @NonNull
    public static <T> T getOrThrowBadRequestIFNull(String msg, T object) {
        throwBadRequestIF(null, msg, object == null);
        return object;
    }

    public static void throwForbiddenIF(org.slf4j.Logger logger, String msg, boolean condition) {
        if (condition)
            throwAnyIF(logger, HttpStatus.FORBIDDEN, msg);
    }

    public static void throwForbiddenIF(String msg, boolean condition) {
        throwForbiddenIF(null, msg, condition);
    }

    @NonNull
    public static <T> T getOrThrowForbiddenIFNull(String msg, T object) {
        throwForbiddenIF(null, msg, object == null);
        return object;
    }

    public static void throwNotFoundIF(org.slf4j.Logger logger, String msg, boolean condition) {
        if (condition)
            throwAnyIF(logger, HttpStatus.NOT_FOUND, msg);
    }

    public static void throwNotFoundIF(String msg, boolean condition) {
        throwNotFoundIF(null, msg, condition);
    }

    @NonNull
    public static <T> T getOrThrowNotFoundIFNull(String msg, T object) {
        throwNotFoundIF(null, msg, object == null);
        return object;
    }

    public static void throwUnauthorizedIF(org.slf4j.Logger logger, String msg, boolean condition) {
        if (condition)
            throwAnyIF(logger, HttpStatus.UNAUTHORIZED, msg);
    }

    public static void throwUnauthorizedIF(String msg, boolean condition) {
        throwUnauthorizedIF(null, msg, condition);
    }

    public static void throwInternalServerIF(org.slf4j.Logger logger, String msg, boolean condition) {
        if (condition)
            throwAnyIF(logger, HttpStatus.INTERNAL_SERVER_ERROR, msg);
    }

    public static void throwInternalServerIF(String msg, boolean condition) {
        throwInternalServerIF((org.slf4j.Logger) null, msg, condition);
    }

    public static void throwInternalServerIF(org.slf4j.Logger logger, Throwable throwable, String msg, boolean condition) {
        if (condition)
            throwAnyIF(logger, throwable, HttpStatus.INTERNAL_SERVER_ERROR, msg);
    }

    @NonNull
    public static <T> T getOrThrowInternalServerIFNull(String msg, T object) {
        throwInternalServerIF(null, msg, object == null);
        return object;
    }

    public static class ApiException extends RuntimeException {

        private final HttpStatusCode httpStatus;

        public ApiException(HttpStatusCode httpStatus, String message) {
            super(message);
            this.httpStatus = httpStatus;
        }

        public ApiException(HttpStatusCode httpStatus, Throwable throwable, String message) {
            super(message, throwable);
            this.httpStatus = httpStatus;
        }

        public HttpStatusCode getHttpStatus() {
            return httpStatus;
        }

    }

    public static class ApiNullPointerException extends NullPointerException {

        private final HttpStatusCode httpStatus;

        public ApiNullPointerException(HttpStatusCode httpStatus, String message) {
            super(message);
            this.httpStatus = httpStatus;
        }
        public HttpStatusCode getHttpStatus() {
            return httpStatus;
        }
    }

    public record ErrorReturn(int status, String message) {
        public static ResponseEntity<ErrorReturn> getResponse(HttpStatusCode httpStatus, String message) {
            return new ResponseEntity<>(new ErrorReturn(httpStatus.value(), message), httpStatus);
        }
    }

    public enum HttpStatusOnExceptionEnum {

        NULL_POINTER(NullPointerException.class, HttpStatus.INTERNAL_SERVER_ERROR),
        HTTP_MESSAGE_NOT_WRITABLE(HttpMessageNotWritableException.class, HttpStatus.INTERNAL_SERVER_ERROR),
        TYPE_MISMATCH(TypeMismatchException.class, HttpStatus.BAD_REQUEST),
        HTTP_MESSAGE_NOT_READABLE(HttpMessageNotReadableException.class, HttpStatus.BAD_REQUEST),
        METHOD_ARGUMENT_NOT_VALID(MethodArgumentNotValidException.class, HttpStatus.BAD_REQUEST),
        BIND(BindException.class, HttpStatus.BAD_REQUEST),
        MISSING_SERVLET_REQUEST_PART(MissingServletRequestPartException.class, HttpStatus.BAD_REQUEST),
        MISSING_SERVLET_REQUEST_PARAMETER(MissingServletRequestParameterException.class, HttpStatus.BAD_REQUEST),
        SERVLET_REQUEST_BINDING(ServletRequestBindingException.class, HttpStatus.BAD_REQUEST),
        NO_HANDLER_FOUND(NoHandlerFoundException.class, HttpStatus.NOT_FOUND),
        ASYNC_REQUEST_TIMEOUT(AsyncRequestTimeoutException.class, HttpStatus.SERVICE_UNAVAILABLE),
        HTTP_REQUEST_METHOD_NOT_SUPPORTED(HttpRequestMethodNotSupportedException.class, HttpStatus.METHOD_NOT_ALLOWED),
        HTTP_MEDIATYPE_NOT_SUPPORTED(HttpMediaTypeNotSupportedException.class, HttpStatus.UNSUPPORTED_MEDIA_TYPE),
        HTTP_MEDIATYPE_NOT_ACCEPTABLE(HttpMediaTypeNotAcceptableException.class, HttpStatus.NOT_ACCEPTABLE),
        MISSING_PATHVARIABLE(MissingPathVariableException.class, HttpStatus.INTERNAL_SERVER_ERROR),
        ILLEGAL_ARGUMENT_EXCEPTION(IllegalArgumentException.class, HttpStatus.INTERNAL_SERVER_ERROR),
        ;

        private final Class<? extends Throwable> throwableClass;
        private final HttpStatus httpStatus;

        private HttpStatusOnExceptionEnum(Class<? extends Throwable> throwableClass, HttpStatus httpStatus) {
            this.throwableClass = throwableClass;
            this.httpStatus = httpStatus;
        }

        public static HttpStatus getHttpStatusByException(Throwable throwable) {
            if (throwable == null)
                return HttpStatus.INTERNAL_SERVER_ERROR;

            for (var enumException : HttpStatusOnExceptionEnum.values())
                if (enumException.throwableClass.isInstance(throwable))
                    return enumException.httpStatus;

            return HttpStatus.INTERNAL_SERVER_ERROR;
        }

    }

    @ExceptionHandler({
            Throwable.class,
            ApiException.class, // aplication generic exception
            ErrorResponseException.class, // above any ResponseStatusException.class
            NullPointerException.class,
            HttpRequestMethodNotSupportedException.class,
            HttpMediaTypeNotSupportedException.class,
            HttpMediaTypeNotAcceptableException.class,
            MissingPathVariableException.class,
            MissingServletRequestParameterException.class,
            ServletRequestBindingException.class,
            MissingServletRequestPartException.class,
            NoHandlerFoundException.class,
            TypeMismatchException.class,
            HttpMessageNotReadableException.class,
            HttpMessageNotWritableException.class,
            MethodArgumentNotValidException.class,
            BindException.class,
            IllegalArgumentException.class,
            AsyncRequestTimeoutException.class
    })
    public ResponseEntity<ErrorReturn> handleThrowableException(Throwable ex) {

        final HttpStatusCode status;

        if (ex instanceof ApiException) {
            status = ((ApiException) ex).getHttpStatus();
        } else if (ex instanceof ErrorResponseException) {
            status = HttpStatus.valueOf(((ErrorResponseException) ex).getStatusCode().value());
        } else {
            status = HttpStatusOnExceptionEnum.getHttpStatusByException(ex);
        }

        if (status.is5xxServerError()) {
            ex.printStackTrace();
        }

        return ErrorReturn.getResponse(status, ex.getMessage());
    }

}
