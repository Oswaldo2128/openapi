package mx.conacyt.crip.ejemplos.openapi.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import mx.conacyt.crip.ejemplos.openapi.domain.UserPet;
import mx.conacyt.crip.ejemplos.openapi.repository.UserPetRepository;
import mx.conacyt.crip.ejemplos.openapi.service.UserPetService;
import mx.conacyt.crip.ejemplos.openapi.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link mx.conacyt.crip.ejemplos.openapi.domain.UserPet}.
 */
@RestController
@RequestMapping("/api")
public class UserPetResource {

    private final Logger log = LoggerFactory.getLogger(UserPetResource.class);

    private static final String ENTITY_NAME = "userPet";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserPetService userPetService;

    private final UserPetRepository userPetRepository;

    public UserPetResource(UserPetService userPetService, UserPetRepository userPetRepository) {
        this.userPetService = userPetService;
        this.userPetRepository = userPetRepository;
    }

    /**
     * {@code POST  /user-pets} : Create a new userPet.
     *
     * @param userPet the userPet to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userPet, or with status {@code 400 (Bad Request)} if the userPet has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-pets")
    public ResponseEntity<UserPet> createUserPet(@Valid @RequestBody UserPet userPet) throws URISyntaxException {
        log.debug("REST request to save UserPet : {}", userPet);
        if (userPet.getId() != null) {
            throw new BadRequestAlertException("A new userPet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserPet result = userPetService.save(userPet);
        return ResponseEntity
            .created(new URI("/api/user-pets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-pets/:id} : Updates an existing userPet.
     *
     * @param id the id of the userPet to save.
     * @param userPet the userPet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userPet,
     * or with status {@code 400 (Bad Request)} if the userPet is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userPet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-pets/{id}")
    public ResponseEntity<UserPet> updateUserPet(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UserPet userPet
    ) throws URISyntaxException {
        log.debug("REST request to update UserPet : {}, {}", id, userPet);
        if (userPet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userPet.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userPetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UserPet result = userPetService.update(userPet);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userPet.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /user-pets/:id} : Partial updates given fields of an existing userPet, field will ignore if it is null
     *
     * @param id the id of the userPet to save.
     * @param userPet the userPet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userPet,
     * or with status {@code 400 (Bad Request)} if the userPet is not valid,
     * or with status {@code 404 (Not Found)} if the userPet is not found,
     * or with status {@code 500 (Internal Server Error)} if the userPet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/user-pets/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserPet> partialUpdateUserPet(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UserPet userPet
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserPet partially : {}, {}", id, userPet);
        if (userPet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userPet.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userPetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserPet> result = userPetService.partialUpdate(userPet);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userPet.getId().toString())
        );
    }

    /**
     * {@code GET  /user-pets} : get all the userPets.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userPets in body.
     */
    @GetMapping("/user-pets")
    public List<UserPet> getAllUserPets(@RequestParam(required = false) String filter) {
        if ("customer-is-null".equals(filter)) {
            log.debug("REST request to get all UserPets where customer is null");
            return userPetService.findAllWhereCustomerIsNull();
        }
        log.debug("REST request to get all UserPets");
        return userPetService.findAll();
    }

    /**
     * {@code GET  /user-pets/:id} : get the "id" userPet.
     *
     * @param id the id of the userPet to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userPet, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-pets/{id}")
    public ResponseEntity<UserPet> getUserPet(@PathVariable Long id) {
        log.debug("REST request to get UserPet : {}", id);
        Optional<UserPet> userPet = userPetService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userPet);
    }

    /**
     * {@code DELETE  /user-pets/:id} : delete the "id" userPet.
     *
     * @param id the id of the userPet to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-pets/{id}")
    public ResponseEntity<Void> deleteUserPet(@PathVariable Long id) {
        log.debug("REST request to delete UserPet : {}", id);
        userPetService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
