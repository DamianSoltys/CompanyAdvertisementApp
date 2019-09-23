//package local.project.Inzynierka;
//
//
//import local.project.Inzynierka.persistence.entity.User;
//import local.project.Inzynierka.servicelayer.dto.Address;
//import local.project.Inzynierka.servicelayer.dto.BecomeNaturalPersonDto;
//import local.project.Inzynierka.servicelayer.dto.UserRegistrationDto;
//import local.project.Inzynierka.servicelayer.dto.Voivodeship;
//import local.project.Inzynierka.servicelayer.services.UserFacade;
//import local.project.Inzynierka.shared.utils.SimpleJsonFromStringCreator;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.boot.web.server.LocalServerPort;
//import org.springframework.http.HttpStatus;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.TimeUnit;
//
//import static org.junit.Assert.assertEquals;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@Transactional
//@Slf4j
//public class UserBecomeNaturalPersonIntegrationTest {
//
//    @Autowired
//    private TestRestTemplate testRestTemplate;
//
//    @Autowired
//    private UserFacade userFacade;
//
//    @LocalServerPort
//    private int randomServerPort;
//
//    private CountDownLatch countDownLatch = new CountDownLatch(1);
//
//    private User user;
//
//
//    String email = "example100@gmail.com";
//    String name = "somename";
//    String password = "Haslo1";
//
//    @Before
//    public void setUp() throws InterruptedException {
//
//
//        var userRegistrationDto = UserRegistrationDto.builder()
//                .email(email)
//                .name(name)
//                .password(password).build();
//
//        String uri = "http://localhost:"+randomServerPort;
//        testRestTemplate.postForEntity(
//                 uri+ "/auth/registration", userRegistrationDto, String.class);
//
//
//        /*
//         *  IMPORTANT - WAIT FOR COMPLETION OF ASYNCHRONOUS EMAIL SENDING
//         *
//         * */
//        countDownLatch.await(1000, TimeUnit.MILLISECONDS);
//
//        user = userFacade.findByName(name);
//        log.info(String.valueOf(user));
//        String token = user.getVerificationToken().getToken();
//
//        testRestTemplate.getForObject(
//                uri+"/auth/registration/confirm?token="+token, String.class);
//
//        user = userFacade.findByName(name);
//        log.info(String.valueOf(user));
//    }
//
//    @Test
//    public void test(){
//
//        BecomeNaturalPersonDto becomeNaturalPersonDto = BecomeNaturalPersonDto.builder()
//                .address(new Address(Voivodeship.LUBELSKIE, "Lublin", "ulica", "43", "5d"))
//                .firstName("Janek")
//                .lastName("Jankowski")
//                .phoneNo("123456789")
//                .build();
//
//
//        String uri = "http://localhost:"+randomServerPort;
//
//        var result= testRestTemplate.withBasicAuth(email,password)
//                .postForEntity(uri+ String.format("api/user/%d/naturalperson", user.getId()),becomeNaturalPersonDto, String.class);
//
//        assertEquals(HttpStatus.CREATED,result.getStatusCode() );
//        assertEquals(SimpleJsonFromStringCreator.toJson("OK"), result.getBody());
//    }
//}
