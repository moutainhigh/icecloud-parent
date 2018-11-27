package com.icetech.authcenter.server.config;

import com.icetech.authcenter.server.domain.AuthClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @Auther: lisc
 * @Date: 2018/11/2 17:07
 * @Description: 所有客户端信息查询service
 */
@Component
@ConfigurationProperties(prefix = "auth.client")
public class ClientConfig {

    private List<AuthClient> list = new ArrayList<>();

    public List<AuthClient> getList() {
        return list;
    }

    public void setList(List<AuthClient> list) {
        this.list = list;
    }

    /**
     * 通过服务名称查询服务
     * @param name
     * @return
     */
    public Optional<AuthClient> getByName(String name){
        if (list == null || list.size()==0){
            return Optional.empty();
        }
        if (StringUtils.isBlank(name)){
            return Optional.empty();
        }
        return list.stream().filter(authClient-> name.equals(authClient.getName())).findAny();
    }
}
