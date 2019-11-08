package com.weickdev.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
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
    public String home() {
        return "welcome to here.";
    }

    @GetMapping("logout")
    public String logout() {
        SecurityUtils.getSubject().logout();
        return "logout success.";
    }

    @GetMapping("hello/{word}")
    public String hello(@PathVariable String word) {
        return "hello " + word;
    }

    @GetMapping("admin")
    public String admin() {
        return "you have admin role";
    }

    @GetMapping("user")
    public String user() {
        return "you have user permission";
    }
}
