package local.project.Inzynierka;


import local.project.Inzynierka.web.controller.AuthenticationController;
import local.project.Inzynierka.web.security.UserBasicAuthenticationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(MockitoJUnitRunner.class)
public class RegistrationTest {

    private MockMvc mockMvc;

    @Mock
    private UserBasicAuthenticationService mock;

    @InjectMocks
    private AuthenticationController authenticationController;

    @Captor
    ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);


    @Before
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController)
                .build();
    }

    @Test
    public void testPassingCorrectParameterWhenCallingConfirmUserInTheService() throws Exception {

        String someToken="abcedf";

        mockMvc.perform(
                get("/user/registration/confirm?token="+someToken));

        verify(mock).confirmUser(argumentCaptor.capture());
        assertEquals(someToken, argumentCaptor.getValue());

    }
}
