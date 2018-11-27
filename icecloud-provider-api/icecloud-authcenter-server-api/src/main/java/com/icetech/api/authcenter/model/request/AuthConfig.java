package com.icetech.api.authcenter.model.request;

import com.icetech.common.annotation.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Auther: lisc
 * @Date: 2018/10/24 18:08
 * @Description:
 */
@Data
@ConfigurationProperties(prefix = "auth.client")
@Component
public class AuthConfig {
    @NotNull
    private String name;
    @NotNull
    private String secret;
}
