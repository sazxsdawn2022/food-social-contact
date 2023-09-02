package com.imooc.oauth2.server.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 客户端配置类，注入到Spring环境中
 */
@Component
@ConfigurationProperties(prefix = "client.oauth2")  //从application.yml加载这个前缀，就是配置文件中的。名字转成驼峰形式的了
@Data
public class ClientOAuth2DataConfiguration {

    // 客户端标识 ID
    private String clientId;

    // 客户端安全码
    private String secret;

    // 授权类型
    private String[] grantTypes;

    // token有效期
    private int tokenValidityTime;

    // refresh-token有效期
    private int refreshTokenValidityTime;

    // 客户端访问范围
    private String[] scopes;

}