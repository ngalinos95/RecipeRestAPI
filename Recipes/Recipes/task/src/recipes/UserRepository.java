package recipes;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.*;


@Component
public interface UserRepository extends CrudRepository<User, Long> {
    User findByEmail(String email);

}


