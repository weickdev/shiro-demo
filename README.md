本例子通过最简单的用户名密码作为校验，并以用户名作为权限和角色名，演示了登录，登出，角色和权限的功能。为了演示方便，所有接口都用json数据返回。集成步骤：

# 增加shiro依赖
```xml
<dependency>
    <groupId>org.apache.shiro</groupId>
    <artifactId>shiro-spring-boot-web-starter</artifactId>
    <version>1.4.1</version>
</dependency>
```

# 自定义Realm
```java
public class SimpleRealm extends AuthorizingRealm {

    /**
     * 授权(每次接口需要授权，都会进入这个方法)
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        // 这里为了演示方便，直接用用户名作为角色和权限
        authorizationInfo.addRole(principals.toString());
        authorizationInfo.addStringPermission(principals.toString());
        return authorizationInfo;
    }

    /**
     * 认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String username = token.getUsername();
        String password = "123456";
        return new SimpleAuthenticationInfo(username, password, getName());
    }
}
```
线上一般需要对密码进行加密，这个时候可以自定义密码匹配器，例如：
```
@Bean
public HashedCredentialsMatcher credentialsMatcher() {
    HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
    credentialsMatcher.setHashAlgorithmName(Sha256Hash.ALGORITHM_NAME);  // 散列算法，这里使用更安全的sha256算法
    credentialsMatcher.setStoredCredentialsHexEncoded(false);  // 数据库存储的密码字段使用HEX还是BASE64方式加密
    credentialsMatcher.setHashIterations(1);  // 散列迭代次数
    return credentialsMatcher;
}
```
在创建realm的时候注入，具体见后文。

# 配置shiro
```java
@Configuration
public class SimpleRealm {
    @Bean
    public SimpleRealm simpleRealm(){
        SimpleRealm simpleRealm = new SimpleRealm();
        //这里用了最简单的密码明文比较，通常需要对密码加密，比如上面的credentialsMatcher()
        simpleRealm.setCredentialsMatcher(new SimpleCredentialsMatcher());
        return simpleRealm;
    }

    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
        chainDefinition.addPathDefinition("/doLogin", "anon");
        //下面是测试权限用的，角色admin和权限user
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
```

# 配置一些常见的连接
```yaml
shiro:
  loginUrl: /login
  successUrl: /home
```