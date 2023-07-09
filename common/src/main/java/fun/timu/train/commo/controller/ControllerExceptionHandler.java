package fun.timu.train.commo.controller;


import fun.timu.train.commo.exception.BusinessException;
import fun.timu.train.commo.response.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.validation.BindException;

/**
 * 统一异常处理、数据预处理等
 */
@ControllerAdvice
public class ControllerExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    /**
     * 参数校验异常统一处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public BaseResponse validExceptionHandler(BindException e) {
        BaseResponse response = new BaseResponse();
        LOG.warn("参数校验失败：{}", e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        response.setSuccess(false);
        response.setMessage(e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return response;
    }

    /**
     * 校验业务异常统一处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public BaseResponse validExceptionHandler(BusinessException e) {
        BaseResponse response = new BaseResponse();
        LOG.warn("业务异常：{}", e.getCode().getDesc());
        response.setSuccess(false);
        response.setMessage(e.getCode().getDesc());
        return response;
    }

    /**
     * 校验系统异常统一处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public BaseResponse validExceptionHandler(Exception e) {
        BaseResponse response = new BaseResponse();
        LOG.error("系统异常：", e);
        response.setSuccess(false);
        response.setMessage("系统出现异常，请联系管理员");
        return response;
    }
}
