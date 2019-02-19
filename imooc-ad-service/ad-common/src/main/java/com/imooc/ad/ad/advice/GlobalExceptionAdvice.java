package com.imooc.ad.ad.advice;

import com.imooc.ad.ad.exception.AdExpection;
import com.imooc.ad.ad.vo.CommonResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionAdvice {
    @ExceptionHandler(value = AdExpection.class)
    public CommonResponse<String> handlerAdException(HttpServletRequest request,
                                                     AdExpection ex) {
        CommonResponse<String> response = new CommonResponse<>(-1, "error");
        response.setData(ex.getMessage());
        return response;
    }
}
