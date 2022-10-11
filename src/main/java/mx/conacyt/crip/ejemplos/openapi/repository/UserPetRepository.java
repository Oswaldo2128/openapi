package mx.conacyt.crip.ejemplos.openapi.repository;

import java.util.Optional;
import mx.conacyt.crip.ejemplos.openapi.domain.User;
import mx.conacyt.crip.ejemplos.openapi.domain.UserPet;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the UserPet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserPetRepository extends JpaRepository<UserPet, Long> {
    /**
     * Find a UserPet by its User entity.
     * @param user User entity.
     * @return The UserPet.
     */
    Optional<UserPet> findByUser(User user);
}
