package local.project.Inzynierka.web.resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PATCH;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController
public class TestResource {

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

