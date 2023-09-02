package com.imooc.oauth2.server.config;

import cn.hutool.crypto.digest.DigestUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.annotation.Resource;

/**
 * Security 配置类
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter { //适配器的父类

    // 注入 Redis 连接工厂
    @Resource
    private RedisConnectionFactory redisConnectionFactory;

    //自己初始化RedisTokenStore，要用到redis连接工厂
    // 初始化 RedisTokenStore 用于将 token 存储至 Redis
    @Bean
    public RedisTokenStore redisTokenStore() {
        RedisTokenStore redisTokenStore = new RedisTokenStore(redisConnectionFactory);
        redisTokenStore.setPrefix("TOKEN:"); // 设置key的层级前缀，方便查询
        return redisTokenStore;
    }

    // 初始化密码编码器，用 MD5 加密密码
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            /**
             * 加密
             * @param rawPassword 原始密码
             * @return
             */
            @Override
            public String encode(CharSequence rawPassword) {
                return DigestUtil.md5Hex(rawPassword.toString());
            }

            /**
             * 校验密码
             * @param rawPassword       原始密码
             * @param encodedPassword   加密密码
             * @return
             */
            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return DigestUtil.md5Hex(rawPassword.toString()).equals(encodedPassword);
            }
        };
    }

    // 初始化认证管理对象
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    // 放行和认证规则，哪些请求放行
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //csrf是一个防御机制，禁用一下，它认为除了get之外的请求都是不安全的
        http.csrf().disable()
                .authorizeRequests()
                // 放行的请求
                //Security环境搭好之后，内部会有一些端点，内部的端点都是OAuths开头的，把这些放行
                //查看端点是通过/actuator，健康检查，度量指标
                .antMatchers("/oauth/**", "/actuator/**").permitAll()
                .and()
                .authorizeRequests()
                // 其他请求必须认证才能访问
                .anyRequest().authenticated();
    }

}
