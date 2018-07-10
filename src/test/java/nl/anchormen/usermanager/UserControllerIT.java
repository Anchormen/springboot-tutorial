package nl.anchormen.usermanager;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Base64;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIT
{
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    public void testGetUsersUnauthorized() throws Exception {
        this.mockMvc.perform(get("/users")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
    }
    
    @Test
    public void testGetUsers() throws Exception {
        this.mockMvc.perform(get("/users")
            .header("Authorization", "Basic " + Base64.getEncoder().encodeToString("pietje@test.nl:pietje".getBytes()))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }
}
