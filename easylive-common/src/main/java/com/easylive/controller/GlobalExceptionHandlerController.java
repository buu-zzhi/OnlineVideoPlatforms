package com.easylive.controller;

import com.easylive.entity.enums.ResponseEnum;
import com.easylive.entity.vo.ResponseVO;
import com.easylive.exception.BusinessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolationException;
import java.net.BindException;

//@RestControllerAdvice
public class GlobalExceptionHandlerController {
    private static final String STATUS_ERROR = "error";

//    @ExceptionHandler(value = Exception.class)
    public ResponseVO handlerException(Exception e) {
        ResponseVO ajaxResponse = new ResponseVO();
        if (e instanceof NoHandlerFoundException) {
            ajaxResponse = ResponseVO.builder()
                    .code(ResponseEnum.CODE_404.getCode())
                    .info(ResponseEnum.CODE_404.getMsg())
                    .status(STATUS_ERROR)
                    .build();
        } else if (e instanceof BusinessException) {
            BusinessException biz = (BusinessException) e;
            ajaxResponse = ResponseVO.builder()
                    .code(biz.getCode() == null ? ResponseEnum.CODE_600.getCode() :  biz.getCode())
                    .info(biz.getMessage())
                    .status(STATUS_ERROR)
                    .build();
        } else if (e instanceof BindException || e instanceof MethodArgumentTypeMismatchException) {
            // 参数类型错误
            ajaxResponse = ResponseVO.builder()
                    .code(ResponseEnum.CODE_600.getCode())
                    .info(ResponseEnum.CODE_600.getMsg())
                    .status(STATUS_ERROR)
                    .build();
        } else if (e instanceof DuplicateKeyException) {
            // 主键冲突
            ajaxResponse = ResponseVO.builder()
                    .code(ResponseEnum.CODE_601.getCode())
                    .info(ResponseEnum.CODE_601.getMsg())
                    .status(STATUS_ERROR)
                    .build();
        } else if (e instanceof ConstraintViolationException) {
            ajaxResponse = ResponseVO.builder()
                    .code(ResponseEnum.CODE_600.getCode())
                    .info(ResponseEnum.CODE_600.getMsg())
                    .status(STATUS_ERROR)
                    .build();
        } else {
            ajaxResponse = ResponseVO.builder()
                    .code(ResponseEnum.CODE_500.getCode())
                    .info(ResponseEnum.CODE_500.getMsg())
                    .status(STATUS_ERROR)
                    .build();
        }
        return ajaxResponse;
    }
}
