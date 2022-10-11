package mx.conacyt.crip.ejemplos.openapi.service;

import java.util.List;
import java.util.Optional;
import mx.conacyt.crip.ejemplos.openapi.domain.Pet;

/**
 * Service Interface for managing {@link Pet}.
 */
public interface PetService {
    /**
     * Save a pet.
     *
     * @param pet the entity to save.
     * @return the persisted entity.
     */
    Pet save(Pet pet);

    /**
     * Updates a pet.
     *
     * @param pet the entity to update.
     * @return the persisted entity.
     */
    Pet update(Pet pet);

    /**
     * Partially updates a pet.
     *
     * @param pet the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Pet> partialUpdate(Pet pet);

    /**
     * Get all the pets.
     *
     * @return the list of entities.
     */
    List<Pet> findAll();

    /**
     * Get the "id" pet.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Pet> findOne(Long id);

    /**
     * Delete the "id" pet.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
