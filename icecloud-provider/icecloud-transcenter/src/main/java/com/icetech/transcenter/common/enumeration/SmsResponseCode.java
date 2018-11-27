package com.icetech.transcenter.common.enumeration;

import lombok.Getter;

/**
 * 短信平台返回码
 */
public enum SmsResponseCode {

    ERROR_00("00","批量短信提交成功"),
    ERROR_01("01",	"提交成功"),
    ERROR_02("02",	"IP验证失败"),
    ERROR_03("03",	"单条短信提交成功"),
    ERROR_04("04",	"用户名错误"),
    ERROR_05("05",	"密码错误"),
    ERROR_06("06",	"参数有误"),
    ERROR_07("07",	" SendTime格式错误"),
    ERROR_08("08",	"短信内容为空"),
    ERROR_09("09",	"手机号码为空"),
    ERROR_10("10",	"AppendID格式错误"),
    ERROR_500("-1",	"提交异常"),
    ;
    private @Getter
    String code;
    private @Getter
    String desc;
    SmsResponseCode(String code,String desc){
        this.desc = desc;
        this.code = code;
    }

    public static String getDescByCode(String code){
        SmsResponseCode[] values = SmsResponseCode.values();
        for (int i = 0;i<values.length;i++){
            if (code.equals(values[i].getCode())){
                return values[i].getDesc();
            }
        }
        return null;
    }
}
