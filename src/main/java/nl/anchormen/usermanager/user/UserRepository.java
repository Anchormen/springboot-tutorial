package nl.anchormen.usermanager.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

// @Repository allows Spring to detect this class as a bean(@Repository is a special type of @Component).
// Annotating with @Repository in stead of @Component translates platform specific persistence exceptions to Spring exceptions.  
@Repository
public interface UserRepository extends CrudRepository<User, Long>
{
    /**
     * Spring will automatically generate a query for finding a user by its email address
     * @param email
     * @return
     */
    User findByEmail(String email);
}
