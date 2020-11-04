package com.kiyotabgangers.resourceserverforblog;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author KIYOTA, Takeshi
 */
@RestController
@RequestMapping("/status")
public class UserController {

    @GetMapping("/check")
    public String status(){
        return "working...";
    }
}
