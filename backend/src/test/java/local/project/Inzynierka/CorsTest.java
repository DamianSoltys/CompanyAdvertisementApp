package local.project.Inzynierka;

import local.project.Inzynierka.web.controller.TestController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CorsTest {

    @Autowired
    private TestController testController;

   @Autowired
   private MockMvc mockMvc;

    @Test
    public void contextLoads() throws Exception{
        assertThat(testController).isNotNull();
    }

    @Test
    public void testEndpointShouldReturn403ErrorWhenCrossOriginSetImproperly() throws Exception {
        this.mockMvc.perform(get("/test-cors").header("Origin", "http://localhost:4201"))
                .andExpect(status().is(403));
    }

    @Test
    public void testEndpointShouldReturn200ResponseWhenCrossOriginSetProperly() throws Exception {
        this.mockMvc.perform(get("/test-cors").header("Origin", "http://localhost:4200"))
                .andExpect(status().is(200));
    }

    @Test
    public void testEndpointShouldReturn403ResponseWhenUsingUnsupportedMethod() throws Exception {
        this.mockMvc.perform(options("/test-cors-methods").header("Origin", "http://localhost:4200"))
                .andExpect(status().is(403));
    }

    @Test
    public void testEndpointShouldReturn200ResponseWhenUsingSupportedMethod() throws Exception {
        this.mockMvc.perform(put("/test-cors-methods").header("Origin", "http://localhost:4200"))
                .andExpect(status().is(200));

        this.mockMvc.perform(post("/test-cors-methods").header("Origin", "http://localhost:4200"))
                .andExpect(status().is(200));

        this.mockMvc.perform(get("/test-cors-methods").header("Origin", "http://localhost:4200"))
                .andExpect(status().is(200));

        this.mockMvc.perform(patch("/test-cors-methods").header("Origin", "http://localhost:4200"))
                .andExpect(status().is(200));

        this.mockMvc.perform(delete("/test-cors-methods").header("Origin", "http://localhost:4200"))
                .andExpect(status().is(200));
    }
}
