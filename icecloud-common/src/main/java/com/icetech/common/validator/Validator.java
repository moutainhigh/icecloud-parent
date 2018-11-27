package com.icetech.common.validator;

import com.icetech.common.ToolsUtil;
import com.icetech.common.annotation.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Description : 参数校验类
 * @author fangct
 * @since 1.0
 */
public class Validator {

    private static Logger logger = LoggerFactory.getLogger(Validator.class);

    //关系表达式关键字
    private static final String AND = "AND";
    private static final String OR = "OR";
    private static final String[] RELATION_TARGET_KEY = {" and "," or "};
    private static final String[] RELATION_REPLACEMENT_KEY = {" AND "," OR "};
    private static final String EQUAL_EXPRESSION = "==";

    public static boolean validate(Object instance) throws NoSuchFieldException, IllegalAccessException {
        if (instance == null){
            return false;
        }
        Class clz = instance.getClass();// 得到类对象
        Field[] fs = clz.getDeclaredFields();//得到属性集合
        List<String> msgList = new ArrayList<String>();
        StringBuffer msgString = new StringBuffer("<参数校验> 校验失败，参数：");
        for (Field field : fs) {//遍历属性
            field.setAccessible(true); // 设置属性是可以访问的(私有的也可以)

            dealNotNull(clz, instance, field, msgList);
            //AnyOneNotNull aon = (AnyOneNotNull) f.getAnnotation(AnyOneNotNull.class);
        }

        for (int i = 0;i < msgList.size();i ++){
            msgString.append(msgList.get(i)).append(",");
        }
        if (msgList.size() > 0){
            logger.info(msgString.toString());
            return false;
        }
        return true;
    }

    /**
     * 处理必传参数的校验，
     * 条件表达式暂时只支持中有一种关系表达式的情况（要么全部是AND，要么全部是OR），
     * 不支持既有AND 又有OR的写法
     * @param clz
     * @param instance
     * @param field
     * @param list
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private static void dealNotNull(Class clz, Object instance, Field field, List list) throws NoSuchFieldException, IllegalAccessException {
        NotNull t = field.getAnnotation(NotNull.class);
        boolean flag = true;
        if(t != null) {
            String condition = t.condition();

            if (ToolsUtil.isNotNull(condition)) {

                String newConditionStr = condition.trim();
                if (newConditionStr.contains(" and ") || newConditionStr.contains(" AND ")
                        || newConditionStr.contains(" or ") || newConditionStr.contains(" OR ") ){
                    for (int i = 0; i < RELATION_TARGET_KEY.length; i++) {
                        newConditionStr = newConditionStr.replace(RELATION_TARGET_KEY[i], RELATION_REPLACEMENT_KEY[i]);
                    }
                    logger.debug("<参数校验> 替换大写后的newConditionStr：{}", newConditionStr);
                    if (newConditionStr.contains(AND)) {
                        String[] andArr = newConditionStr.split(AND);
                        for (String andStr : andArr) {
                            String[] equalArr = andStr.split(EQUAL_EXPRESSION);
                            String conName = equalArr[0].trim();
                            String conValue = equalArr[1].trim();

                            Field nameField = clz.getDeclaredField(conName);
                            nameField.setAccessible(true);
                            Object fieldValue = nameField.get(instance);
                            if (!conValue.equals(fieldValue)) {
                                if (conValue.startsWith("*")){
                                    String temp = conValue.substring(1);
                                    if (String.valueOf(fieldValue).endsWith(temp)){
                                        continue;
                                    }
                                }
                                flag = false;
                                break;
                            }
                        }
                    } else {
                        String[] orArr = newConditionStr.split(OR);
                        for (int j=0;j<orArr.length;j++) {
                            String orStr = orArr[j];
                            String[] equalArr = orStr.split(EQUAL_EXPRESSION);
                            String conName = equalArr[0].trim();
                            String conValue = equalArr[1].trim();

                            Field nameField = clz.getDeclaredField(conName);
                            nameField.setAccessible(true);
                            Object fieldValue = nameField.get(instance);
                            if (conValue.equals(fieldValue)) {
                                break;
                            }else if(j == orArr.length -1){
                                flag = false;
                            }else{
                                if (conValue.startsWith("*")){
                                    String temp = conValue.substring(1);
                                    if (String.valueOf(fieldValue).endsWith(temp)){
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }else{
                    String[] equalArr = newConditionStr.split(EQUAL_EXPRESSION);
                    String conName = equalArr[0].trim();
                    String conValue = equalArr[1].trim();

                    Field nameField = clz.getDeclaredField(conName);
                    nameField.setAccessible(true);
                    Object fieldValue = nameField.get(instance);
                    if (!conValue.equals(fieldValue)) {
                        if (conValue.startsWith("*")){
                            String temp = conValue.substring(1);
                            if (!String.valueOf(fieldValue).endsWith(temp)){
                                flag = false;
                            }
                        }else{
                            flag = false;
                        }
                    }
                }
            }
            if (flag){
                Object val = field.get(instance);// 得到此属性的值
                if (ToolsUtil.isNull(val)) {
                    list.add(field.getName());
                }
            }
        }
    }
}
