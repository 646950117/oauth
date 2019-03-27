package com.example.oauth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {
    private Logger logger = LoggerFactory.getLogger(AuthorizationServerConfiguration.class);

    private static final String ENV_OAUTH = "authentication.oauth.";
    private static final String PROP_CLIENTID = "clientid";
    private static final String PROP_SECRET = "secret";
    private static final String PROP_TOKEN_VALIDITY_SECONDS = "tokenValidityInSeconds";

    @Autowired
    @Qualifier("authenticationManagerBean")//认证方式
    private AuthenticationManager authenticationManager ;


    @Bean
    public TokenStore tokenStore(){

        //这个是基于JDBC的实现，令牌（Access Token）会保存到数据库
        return new InMemoryTokenStore();
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(tokenStore())
                .authenticationManager(authenticationManager) ;
    }

    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory() // 使用in-memory存储
                .withClient("")//client_id用来标识客户的Id
                .scopes("read", "write") //允许授权范围
                .authorities("ROLE_ADMIN","ROLE_USER")//客户端可以使用的权限
                .authorizedGrantTypes("password", "refresh_token")//允许授权类型
                .secret("")//secret客户端安全码
                .accessTokenValiditySeconds(1800);

    }





}
