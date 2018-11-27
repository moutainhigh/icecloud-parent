package com.icetech.transcenter.config.exception;


import com.icetech.common.ResultTools;
import com.icetech.common.constants.CodeConstants;
import com.icetech.common.exception.ResponseBodyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局的的异常拦截器
 * @author wangzw
 */
@ControllerAdvice
public class GlobalExceptionHandler {
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);


	/**
	 * 全局异常.
	 *
	 * @param e the e
	 *
	 * @return the String
	 */
	@ExceptionHandler(Exception.class)
    @ResponseBody
	public String exception(Exception e) {
		logger.error("保存全局异常信息 ex={}", e.getMessage(), e);
		return ResultTools.setResponse(CodeConstants.ERROR,CodeConstants.getName(CodeConstants.ERROR));
	}

	/**
	 * 业务异常.
	 *
	 * @param e the e
	 *
	 * @return the String
	 */
	@ExceptionHandler(ResponseBodyException.class)
	@ResponseBody
	public String businessException(ResponseBodyException e) {
		logger.error("业务异常={}", e.getMessage());
		return ResultTools.setResponse(e.getErrCode(),CodeConstants.getName(e.getErrCode()) + "," + e.getMessage());
	}

}
