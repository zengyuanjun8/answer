package com.zyj.answer.controller;

import com.alibaba.fastjson.JSONObject;
import com.zyj.answer.util.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.StreamUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

@RestControllerAdvice
@Slf4j
public class ControllerExceptionHandler extends RequestBodyAdviceAdapter {

    public static final String REQUEST_BODY_ATTRIBUTE_KEY = "requestBody";
    private final HttpServletRequest request;
    private final HttpServletResponse response;

    public ControllerExceptionHandler(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        recordLog(e);
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        FieldError fieldError = e.getBindingResult().getFieldError();
        return fieldError == null ? "参数错误" : fieldError.getDefaultMessage();
    }

    @ExceptionHandler(Exception.class)
    public String exceptionHandler(Exception e) {
        recordLog(e);
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return e.getMessage();
    }

    private void recordLog(Exception e) {
        JSONObject jsonLog = new JSONObject();
        jsonLog.put("requestUri", request.getRequestURI());
        jsonLog.put("method", request.getMethod());
        jsonLog.put("parameterMap", request.getParameterMap());
        jsonLog.put("requestBody", request.getAttribute(REQUEST_BODY_ATTRIBUTE_KEY));
        jsonLog.put("ip", RequestUtil.getIpAddress(request));
        log.error(jsonLog.toString(), e);
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        String requestBody = StreamUtils.copyToString(inputMessage.getBody(), StandardCharsets.UTF_8);
        request.setAttribute(REQUEST_BODY_ATTRIBUTE_KEY, requestBody);
        return new HttpInputMessage() {
            @Override
            public InputStream getBody() {
                return new ByteArrayInputStream(requestBody.getBytes());
            }

            @Override
            public HttpHeaders getHeaders() {
                return inputMessage.getHeaders();
            }
        };
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return HttpMethod.POST.matches(request.getMethod()) || HttpMethod.PUT.matches(request.getMethod());
    }
}
