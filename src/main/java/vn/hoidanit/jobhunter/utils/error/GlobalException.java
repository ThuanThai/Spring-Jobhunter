package vn.hoidanit.jobhunter.utils.error;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import vn.hoidanit.jobhunter.domain.RestResponse;

@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler(value = IdInvalidExeption.class)
    public ResponseEntity<RestResponse<Object>> handleException(IdInvalidExeption idExeption) {

        RestResponse<Object> rs = new RestResponse<Object>();

        rs.setStatusCode(HttpStatus.BAD_REQUEST.value());
        rs.setError("CALL API FAILD");
        rs.setMessage("IdInvalidExeption");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(rs);

    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<RestResponse<Object>> handleException(MethodArgumentNotValidException val) {

        BindingResult bResult = val.getBindingResult();
        final List<FieldError> fieldErrors = bResult.getFieldErrors();
        RestResponse<Object> rs = new RestResponse<Object>();

        rs.setStatusCode(HttpStatus.BAD_REQUEST.value());
        rs.setError(val.getBody().getDetail());
        List<String> errors = fieldErrors.stream().map(f -> f.getDefaultMessage()).collect(Collectors.toList());
        rs.setMessage(errors.size() > 1 ? errors : errors.get(0));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(rs);

    }
}
