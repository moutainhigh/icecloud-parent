package com.icetech.authcenter.client.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Auther: lisc
 * @Date: 2018/10/25 09:37
 * @Description: 在非/api/路径下 强制使用服务间鉴权
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value={ElementType.METHOD,ElementType.TYPE})
public @interface RequireToken {
}
