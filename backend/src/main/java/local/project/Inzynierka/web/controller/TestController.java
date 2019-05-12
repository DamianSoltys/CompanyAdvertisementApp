package local.project.Inzynierka.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
public class TestController {

    @RequestMapping(method = GET, value = "/test")
    String test(){
        return "TEST";
    }

    @RequestMapping(method = GET, value = "/test-cors")
    String testCors(){
        return "TEST";
    }

    @RequestMapping(method = {PUT, GET, POST,DELETE, PATCH}, value = "/test-cors-methods")
    String testCorsMethods(){
        return "TEST";
    }
    @RequestMapping(method = RequestMethod.OPTIONS, value = "/test-cors-methods")
    String testCorsOptionsMethod(){
        return "TEST";
    }
}

