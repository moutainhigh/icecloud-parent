package com.icetech.authcenter.server.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 服务认证信息
 * </p>
 *
 * @author lisc
 * @since 2018-10-25
 */
@Data
public class AuthClient implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 服务名称
     */
    private String name;
    /**
     * 服务密钥
     */
    private String secret;
}
