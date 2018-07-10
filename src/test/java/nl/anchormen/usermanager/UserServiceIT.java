package nl.anchormen.usermanager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import nl.anchormen.usermanager.test.WithCustomMockUser;
import nl.anchormen.usermanager.user.User;
import nl.anchormen.usermanager.user.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceIT
{
    
    @Autowired
    UserService userService;
    
    @Test
    public void testFindUser()
    {
        User user = userService.findByEmail("pietje@test.nl");
        assertEquals(user.getEmail(), "pietje@test.nl");
    }
    
    @Test
    @WithCustomMockUser
    public void testSaveInvalidUser()
    {
        User user = new User();
        try {
            userService.save(user);
            fail("Expected ConstraintViolationException");
        }
        catch(ConstraintViolationException e) {
        }
    }

}
