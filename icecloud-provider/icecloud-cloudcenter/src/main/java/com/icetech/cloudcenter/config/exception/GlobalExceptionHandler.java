package com.icetech.cloudcenter.config.exception;


import com.icetech.common.ResultTools;
import com.icetech.common.constants.CodeConstants;
import com.icetech.common.exception.ResponseBodyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.nio.file.AccessDeniedException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局的的异常拦截器
 * @author wangzw
 */
@ControllerAdvice
public class GlobalExceptionHandler {
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	/**
	 * 参数非法异常.
	 *
	 * @param e the e
	 *
	 * @return the wrapper
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
	public String illegalArgumentException(MethodArgumentNotValidException e) {
		logger.error("参数非法异常={}", e.getMessage(), e);
		return ResultTools.setResponse(CodeConstants.ERROR,CodeConstants.getName(CodeConstants.ERROR));
	}

	/**
	 * 业务异常.
	 *
	 * @param e the e
	 *
	 * @return the wrapper
	 */
	@ExceptionHandler(ResponseBodyException.class)
    @ResponseBody
	public String businessException(ResponseBodyException e) {
		logger.error("业务异常={}", e.getMessage());
		return ResultTools.setResponse(e.getErrCode(),CodeConstants.getName(e.getErrCode()) + "," + e.getMessage());
	}


	/**
	 * 业务异常.
	 *
	 * @param e the e
	 *
	 * @return the wrapper
	 */
	@ExceptionHandler(SQLIntegrityConstraintViolationException.class)
	@ResponseBody
	public String sqlException(SQLIntegrityConstraintViolationException e) {
		logger.error("sql异常={}", e.getMessage());
		return ResultTools.setResponse(CodeConstants.ERROR_405,CodeConstants.getName(CodeConstants.getName(CodeConstants.ERROR_405)));
	}

	/**
	 * 无权限访问.
	 *
	 * @param e the e
	 *
	 * @return the wrapper
	 */
	@ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
	public String unAuthorizedException(AccessDeniedException e) {
		logger.error("无权访问={}", e.getMessage(), e);
		return ResultTools.setResponse(CodeConstants.ERROR,CodeConstants.getName(CodeConstants.ERROR));
	}


	/**
	 * 全局异常.
	 *
	 * @param e the e
	 *
	 * @return the wrapper
	 */
	@ExceptionHandler(Exception.class)
    @ResponseBody
	public String exception(Exception e) {
		logger.error("保存全局异常信息 ex={}", e.getMessage(), e);
		return ResultTools.setResponse(CodeConstants.ERROR,CodeConstants.getName(CodeConstants.ERROR));
	}
	/**
	 * 请求方式异常.
	 *
	 * @param e the e
	 *
	 * @return the wrapper
	 */
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	@ResponseBody
	public String exception(HttpRequestMethodNotSupportedException e) {
		logger.error("请求方式错误 ex={}", e.getMessage());
		return ResultTools.setResponse(CodeConstants.ERROR_403,CodeConstants.getName(CodeConstants.ERROR_403));
	}

}
