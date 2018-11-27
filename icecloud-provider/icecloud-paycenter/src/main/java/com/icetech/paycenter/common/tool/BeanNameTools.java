package com.icetech.paycenter.common.tool;

public class BeanNameTools {

    private static final String CMBC_BEAN_NAME = "payCenter4CmbcServiceImpl";
    private static final String WX_BEAN_NAME = "payCenter4WxServiceImpl";
    private static final String ALI_BEAN_NAME = "payCenter4AliServiceImpl";

    /**
     * 根据支付类型获取不同的实现类的bean name
     * @param selectType
     * @return
     */
    public static String getBeanName(String selectType){
        if (selectType.startsWith("CMBC-")){
            return CMBC_BEAN_NAME;
        }
        if (selectType.startsWith("WX-")){
            return WX_BEAN_NAME;
        }
        if (selectType.startsWith("ALI-")){
            return ALI_BEAN_NAME;
        }
        //默认走民生的配置
        return CMBC_BEAN_NAME;
    }
}
