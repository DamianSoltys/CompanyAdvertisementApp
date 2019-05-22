package local.project.Inzynierka;


import com.fasterxml.jackson.databind.ObjectMapper;
import local.project.Inzynierka.web.controller.AuthenticationController;
import local.project.Inzynierka.web.dto.LoginDto;
import local.project.Inzynierka.web.dto.UserRegistrationDto;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotSame;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class LoginTest {

    private LoginDto loginDto;

    @Autowired
    private AuthenticationController authenticationController;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {

        String name = "name";
        String password = "pass";
        String email = "e@example.com";

        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setEmail(email);
        userRegistrationDto.setName(name);
        userRegistrationDto.setPassword(password);

        loginDto = new LoginDto();
        loginDto.setEmail(email);
        loginDto.setPassword(password);


        ObjectMapper mapper= new ObjectMapper();
        String json = mapper.writeValueAsString(userRegistrationDto);

        mockMvc.perform(
                    post("/user/registration")
                            .content(json).
                    contentType("application/json")).
                andExpect(status().isOk());


    }


    public void loginReturnsTrueWhenSuccessful()  {
        var result = authenticationController.login(loginDto);
        assertEquals("{\"data\":\"OK\"}", result.getBody());
    }

    @Test
    public void loginReturnsFalseWhenFailed()  {
        loginDto.setPassword("X");
        var result =  authenticationController.login(loginDto);
        assertNotSame("OK", result.getBody());
    }



}
