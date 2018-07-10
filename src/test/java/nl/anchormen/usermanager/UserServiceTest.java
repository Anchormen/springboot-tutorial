package nl.anchormen.usermanager;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import nl.anchormen.usermanager.user.User;
import nl.anchormen.usermanager.user.UserRepository;
import nl.anchormen.usermanager.user.UserService;

public class UserServiceTest
{
    @Test
    public void userServiceTest()
    {
        User sjaak = new User();
        sjaak.setId(1L);
        sjaak.setEmail("sjaak@sjaak.nl");
        UserRepository userRepository = mock(UserRepository.class);
        //given
        given(userRepository.findByEmail("sjaak@sjaak.nl")).willReturn(sjaak);
        UserService userService = new UserService(userRepository, new BCryptPasswordEncoder(4));
        
        //when
        User sjaakFromService = userService.findByEmail("sjaak@sjaak.nl");
        
        //then
        assertEquals(sjaak, sjaakFromService);
    }
}
