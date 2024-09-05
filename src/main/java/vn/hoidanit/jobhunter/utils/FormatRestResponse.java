package vn.hoidanit.jobhunter.utils;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import jakarta.servlet.http.HttpServletResponse;
import vn.hoidanit.jobhunter.domain.RestResponse;

@ControllerAdvice
public class FormatRestResponse implements ResponseBodyAdvice {

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
            Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {

        HttpServletResponse httpServletResponse = ((ServletServerHttpResponse) response).getServletResponse();

        RestResponse<Object> rs = new RestResponse<Object>();

        int status = httpServletResponse.getStatus();

        rs.setStatusCode(status);

        if (status >= 400) {
            return body;
        } else {
            rs.setData(body);
            rs.setMessage("CALL API SUCCESS");
        }

        return rs;
    }

}
