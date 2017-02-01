package mav;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by gabor on 2017. 02. 01..
 */

@RestController
public class HelloController {
    @RequestMapping(method = RequestMethod.GET, value = "/", produces = "application/json")
    public String index() {
        return "anya";
    }
}