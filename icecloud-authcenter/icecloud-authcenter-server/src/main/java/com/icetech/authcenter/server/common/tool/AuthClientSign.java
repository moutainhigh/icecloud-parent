package com.icetech.authcenter.server.common.tool;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Auther: lisc
 * @Date: 2018/10/25 18:38
 * @Description: 签名工具
 */
@Slf4j
@Component
public class AuthClientSign {
    @Value("${auth.server.key}")
    private String key ;

    /**
     * 签名
     * @param name
     * @return
     */
    public String sign(String name) {
        log.info("<签名工具>生成签名,签名参数={},签名key={},签名方式=HS256",name,key);
        return Jwts.builder().signWith(SignatureAlgorithm.HS256, key).setExpiration(DateTime.now().plusHours(1).toDate()).setSubject(name).compact();
    }

    /**
     * 解析签名
     * @param token
     * @return
     */
    public String parseToken(String token) {
        log.info("<签名工具>解析签名,参数={},key={}",token,key);
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getSignature();
    }
}
