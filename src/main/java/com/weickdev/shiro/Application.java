package com.weickdev.shiro;

import org.apache.shiro.authc.credential.PasswordMatcher;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.mgt.SessionsSecurityManager;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author : weichengke
 * @date : 2019-11-08 10:53
 */
@SpringBootApplication
public class Application {

    public static void main(String... args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public SimpleRealm simpleRealm(){
        SimpleRealm simpleRealm = new SimpleRealm();
        simpleRealm.setCredentialsMatcher(new SimpleCredentialsMatcher());
        return simpleRealm;
    }

    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
        chainDefinition.addPathDefinition("/doLogin", "anon");
        chainDefinition.addPathDefinition("/admin", "roles[admin]");
        chainDefinition.addPathDefinition("/user", "perms[user]");
        // 注意这里的顺序，如果/**放在前面，那么只要登录了就通过了，后面的权限判断就不生效了
        chainDefinition.addPathDefinition("/**", "authc");

        return chainDefinition;
    }

    @Bean
    public SessionsSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(simpleRealm());
        return securityManager;
    }
}
