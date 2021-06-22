package org.geekbang.springbootgraalvm.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO
 *
 * @author: Ben
 * @since: TODO
 * @date: 2021-06-16
 */
@RestController
public class WebController {

    @RequestMapping("/")
    private String echo(){
        return "Hello,World";
    }

}
