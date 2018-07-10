package nl.anchormen.usermanager.web;

import java.util.Arrays;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import nl.anchormen.usermanager.user.User;
import nl.anchormen.usermanager.user.UserRole;
import nl.anchormen.usermanager.user.UserService;

// A @Controller is a special type of @component that can handle web requests.
// To handle web requests you must use the @Controller annotation, @Component will not work.
// The RestController is a special type of controller for rest applications.
@RestController
public class UserController
{
    private final UserService userService;

    @Autowired
    public UserController(UserService userService)
    {
        this.userService = userService;
    }

    // Swagger documentation, see the swagger section for more details. 
    @ApiOperation("get all users")
    // Maps a list of users to /users, the url for the local application is localhost:8080/users
    @GetMapping("/users")
    public Iterable<User> users()
    {
        return userService.getAll();

    }

    @ApiOperation("save a user")
    @PostMapping("/user")
    /**
     * We do not send the hashed password back and forth to the client, but the
     * application needs to be able to change the password. The SaveUserDto allows
     * the client to send a new password if it wants to change the password, or
     * leave it null when the password should stay the same.
     * 
     * @param saveUserDto
     * @return
     */
    public User saveUser(@RequestBody SaveUserDto saveUserDto)
    {
        User toSave = saveUserDto.user;
        if (saveUserDto.password == null)
        {
            // We want to save the user with its old password.
            userService.fillPassword(toSave);
        } else
        {
            userService.changePassword(toSave, saveUserDto.password);
        }

        return userService.save(toSave);
    }

    @ApiOperation("delete a user")
    @DeleteMapping("/user/{userId}")
    public Long deleteUser(@PathVariable(value = "userId") Long userId, HttpServletResponse res)
    {
        if (userService.delete(userId))
        {
            return userId;
        }
        res.setStatus(HttpServletResponse.SC_NOT_FOUND);
        return -1L;
    }

    @ApiOperation("get all existing user roles")
    @GetMapping("/userRoles")
    public Iterable<UserRole> userRoles()
    {
        return Arrays.asList(UserRole.values());
    }

    /**
     * Special object for saving a user with an optional password.
     */
    static class SaveUserDto
    {
        private User user;
        private String password;

        public User getUser()
        {
            return user;
        }

        public void setUser(User user)
        {
            this.user = user;
        }

        public String getPassword()
        {
            return password;
        }

        public void setPassword(String password)
        {
            this.password = password;
        }

    }
}
