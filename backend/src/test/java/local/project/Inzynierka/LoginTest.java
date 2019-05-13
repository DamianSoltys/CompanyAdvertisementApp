package local.project.Inzynierka;


import local.project.Inzynierka.web.controller.AuthenticationController;
import local.project.Inzynierka.web.dto.LoginDto;
import local.project.Inzynierka.web.dto.UserRegistrationDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class LoginTest {

    private LoginDto loginDto;

    @Autowired
    private AuthenticationController authenticationController;

    @Before
    public void setUp(){

        String name = "name";
        String password = "pass";
        String email = "example@example.com";

        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setEmail(email);
        userRegistrationDto.setName(name);
        userRegistrationDto.setPassword(password);

        loginDto = new LoginDto();
        loginDto.setEmail(email);
        loginDto.setPassword(password);

        authenticationController.registerNewUser(userRegistrationDto);
    }

    @Test
    public void loginReturnsTrueWhenSuccessful(){
        assertTrue(authenticationController.login(loginDto));
    }

    @Test
    public void loginReturnsFalseWhenFailed(){
        loginDto.setPassword("X");
        assertFalse(authenticationController.login(loginDto));
    }



}
