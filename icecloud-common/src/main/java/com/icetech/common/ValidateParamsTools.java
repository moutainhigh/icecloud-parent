package com.icetech.common;

import com.icetech.common.constants.ValidateType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 
* @Description: 参数验证工具类
* @author fangct
* @date 2017年12月12日 下午9:25:09
 */
public class ValidateParamsTools {

	private static Logger logger = LoggerFactory.getLogger(ValidateParamsTools.class);
	
	/**
	 * 验证参数
	 * @param parmasMap
	 * @param paramsArr 
	 * @return
	 */
	public static boolean vaildateParams(Map<String, String> parmasMap, String[] paramsArr, ValidateType type){
		
		boolean success = true;
		
		if (type.equals(ValidateType.必传)) {
			return validateMust(parmasMap, paramsArr);
		}else if (type.equals(ValidateType.任一必传)) {
			return validateAnyone(parmasMap, paramsArr);
		}
		
		return success;
	}

	/**
	 * 验证必传参数
	 * @param parmasMap
	 * @param paramsArr 
	 * @return
	 */
	private static boolean validateMust(Map<String, String> parmasMap, String[] paramsArr){
		StringBuffer logContent = new StringBuffer();
		boolean success = true;
		for(String param : paramsArr){
			String p = parmasMap.get(param);
			if(ToolsUtil.isNull(p)){
				logContent.append(param).append(",");
				success = false;
			}
		}
		if(!success){
			logger.info("***必传参数: {} 为空！", logContent.toString());
		}
		return success;
	}
	
	/**
	 * 验证任一必传
	 * @param parmasMap
	 * @param paramsArr
	 * @return
	 */
	private static boolean validateAnyone(Map<String, String> parmasMap, String[] paramsArr) {
		StringBuffer logContent = new StringBuffer();
		boolean success = false;
		for(String param : paramsArr){
			String p = parmasMap.get(param);
			if(ToolsUtil.isNotNull(p)){
				success = true;
				break;
			}
			logContent.append(param).append(",");
		}
		if(!success){
			logger.info("***任一必传参数: {} 都为空！", logContent.toString());
		}
		return success;
	}
}
