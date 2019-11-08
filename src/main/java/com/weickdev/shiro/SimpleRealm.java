package com.weickdev.shiro;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * @author : weichengke
 * @date : 2019-11-08 11:04
 */
public class SimpleRealm extends AuthorizingRealm {

    /**
     * 授权(每次接口需要授权，都会进入这个方法)
     *
     * @param principals: 这里是用户名
     * @return
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
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String username = token.getUsername();
        String password = "123456";
        return new SimpleAuthenticationInfo(username, password, getName());
    }
}
