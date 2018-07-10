package nl.anchormen.usermanager.user;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;


// @Validated is part of the javax.validation integration, this allows you to validate business objects using annotations 
@Validated
// @Service allows Spring to detect this class as a bean(@Service is a special type of @Component).
// The Service annotation does not have implementation differences with @Component(yet).
@Service
public class UserService
{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


     // Inject both the user repository and the password encoder  using autowired
    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User findByEmail(String email)
    {
        return userRepository.findByEmail(email);
    }

    /**
     * Fill with stored password.
     * 
     * @param user
     */
    public void fillPassword(User user)
    {
        if (user.getId() != null && user.getEncodedPassword() == null)
        {
            userRepository.findById(user.getId())
                .ifPresent(oldValue -> user.setEncodedPassword(oldValue.getEncodedPassword()));
        }
    }

    /**
     * The @PreAuthorize checks if the user has the correct roles before the method
     * is executed. It throws an Exception if the user does not have the right
     * permissions, we configured an accessDeniedHandler in the security configuration to
     * handle this
     */
    @PreAuthorize("hasRole('ADMIN')")
    public User save(@Valid User user)
    {
        return userRepository.save(user);
    }

    public Iterable<User> getAll()
    {
        return userRepository.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void changePassword(User user, @Valid @Size(min = 4) String password)
    {
        user.setEncodedPassword(passwordEncoder.encode(password));
    }

    // This annotation causes database interactions in this method to be executed as a transaction.
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    // javax.validation annotations can also be used on method parameters
    public boolean delete(@Valid @NotNull Long id)
    {
        if (!userRepository.existsById(id))
        {
            return false;
        }
        userRepository.deleteById(id);
        return true;
    }

    /**
     * This method fills the database with some test data if the database it is empty.
     */
    @PostConstruct
    public void init()
    {
        if (getAll().iterator().hasNext())
        {
            return;
        }

        String[] testUserNames = new String[] { "pietje", "jantje", "klaas" };
        for (int i = 0; i < testUserNames.length; i++)
        {
            String name = testUserNames[i];
            User user = new User();
            user.setEmail(name + "@test.nl");
            user.setUserRole(UserRole.USER);
            changePassword(user, name);
            user.setPhoneNumber(String.format("06123456%02d", i));
            save(user);
        }

        User admin = new User();
        admin.setEmail("admin@test.nl");
        changePassword(admin, "admin");
        admin.setUserRole(UserRole.ADMIN);
        save(admin);
    }

}
