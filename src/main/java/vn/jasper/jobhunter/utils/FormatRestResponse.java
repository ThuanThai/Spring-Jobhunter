package vn.jasper.jobhunter.utils;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import jakarta.servlet.http.HttpServletResponse;
import vn.jasper.jobhunter.annotation.ApiMessage;
import vn.jasper.jobhunter.domain.RestResponse;

import java.util.Objects;

@ControllerAdvice
public class FormatRestResponse implements ResponseBodyAdvice {

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
            Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {

        if (body instanceof String) {
            return body;
        }

        HttpServletResponse httpServletResponse = ((ServletServerHttpResponse) response).getServletResponse();

        RestResponse<Object> rs = new RestResponse<Object>();

        int status = httpServletResponse.getStatus();

        rs.setStatusCode(status);

        ApiMessage annotation = returnType.getMethodAnnotation(ApiMessage.class);
        String message = (annotation != null) ? annotation.value() : "Call API Success";

        if (status >= 400) {
            return body;
        } else {
            rs.setData(body);
            rs.setMessage(message);
        }

        return rs;
    }

}
