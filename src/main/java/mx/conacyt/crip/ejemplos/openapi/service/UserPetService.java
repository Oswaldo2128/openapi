package mx.conacyt.crip.ejemplos.openapi.service;

import java.util.List;
import java.util.Optional;
import mx.conacyt.crip.ejemplos.openapi.domain.UserPet;

/**
 * Service Interface for managing {@link UserPet}.
 */
public interface UserPetService {
    /**
     * Save a userPet.
     *
     * @param userPet the entity to save.
     * @return the persisted entity.
     */
    UserPet save(UserPet userPet);

    /**
     * Updates a userPet.
     *
     * @param userPet the entity to update.
     * @return the persisted entity.
     */
    UserPet update(UserPet userPet);

    /**
     * Partially updates a userPet.
     *
     * @param userPet the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UserPet> partialUpdate(UserPet userPet);

    /**
     * Get all the userPets.
     *
     * @return the list of entities.
     */
    List<UserPet> findAll();
    /**
     * Get all the UserPet where Customer is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<UserPet> findAllWhereCustomerIsNull();

    /**
     * Get the "id" userPet.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UserPet> findOne(Long id);

    /**
     * Delete the "id" userPet.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get a UserPet by the username.
     * @param username The username (login in User entity)
     * @return the entity.
     */
    Optional<UserPet> findByUserName(String username);
}
