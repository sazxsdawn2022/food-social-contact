package com.imooc.diners.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 客户端配置类
 */
@Component
@ConfigurationProperties(prefix = "oauth2.client")  //从application.yml中读取
@Getter
@Setter
public class OAuth2ClientConfiguration {

    private String clientId;
    private String secret;
    private String grant_type;  //发请求时就是写成grant_type
    private String scope;

}