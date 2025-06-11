package com.restfulapi.config;

import com.restfulapi.annotation.ApiMessage;
import com.restfulapi.dto.ApiResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class ApiResponseAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(
            Object body,
            MethodParameter returnType,
            MediaType selectedContentType,
            Class selectedConverterType,
            ServerHttpRequest request,
            ServerHttpResponse response) {

        // Nếu body đã là ApiResponse thì không wrap lại nữa
        if (body instanceof ApiResponse) {
            return body;
        }
        // Lấy message từ @ApiMessage nếu có
        String message = "Success"; // mặc định
        ApiMessage apiMessage=returnType.getMethodAnnotation(ApiMessage.class);
        if (apiMessage!=null){
            message=apiMessage.value();
        }
        return new ApiResponse<>(HttpStatus.OK.value(), message, body);
    }
}
