package com.weickdev.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.*;

/**
 * @author : weichengke
 * @date : 2019-11-08 10:54
 */
@RestController
@RequestMapping("")
public class IndexController {

    @GetMapping("login")
    public String login() {
        return "you must login to access this link";
    }

    @GetMapping("doLogin")
    public String doLogin(@RequestParam("username") String username, @RequestParam("password") String password) {
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        SecurityUtils.getSubject().login(token);
        return "login success.";
    }

    @GetMapping("home")
    @RequiresAuthentication
    public String home() {
        return "welcome to here.";
    }

    @GetMapping("logout")
    public String logout() {
        SecurityUtils.getSubject().logout();
        return "logout success.";
    }

    @GetMapping("hello/{word}")
    @RequiresAuthentication
    public String hello(@PathVariable String word) {
        return "hello " + word;
    }

    @GetMapping("admin")
    @RequiresRoles({"admin"})
    public String admin() {
        return "you have admin role";
    }

    @GetMapping("user")
    @RequiresPermissions({"user"})
    public String user() {
        return "you have user permission";
    }
}
