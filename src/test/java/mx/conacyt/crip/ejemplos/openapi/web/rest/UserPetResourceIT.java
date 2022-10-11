package mx.conacyt.crip.ejemplos.openapi.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import mx.conacyt.crip.ejemplos.openapi.IntegrationTest;
import mx.conacyt.crip.ejemplos.openapi.domain.UserPet;
import mx.conacyt.crip.ejemplos.openapi.repository.UserPetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link UserPetResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserPetResourceIT {

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final Integer DEFAULT_USER_STATUS = 1;
    private static final Integer UPDATED_USER_STATUS = 2;

    private static final String ENTITY_API_URL = "/api/user-pets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserPetRepository userPetRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserPetMockMvc;

    private UserPet userPet;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserPet createEntity(EntityManager em) {
        UserPet userPet = new UserPet().phone(DEFAULT_PHONE).userStatus(DEFAULT_USER_STATUS);
        return userPet;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserPet createUpdatedEntity(EntityManager em) {
        UserPet userPet = new UserPet().phone(UPDATED_PHONE).userStatus(UPDATED_USER_STATUS);
        return userPet;
    }

    @BeforeEach
    public void initTest() {
        userPet = createEntity(em);
    }

    @Test
    @Transactional
    void createUserPet() throws Exception {
        int databaseSizeBeforeCreate = userPetRepository.findAll().size();
        // Create the UserPet
        restUserPetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userPet)))
            .andExpect(status().isCreated());

        // Validate the UserPet in the database
        List<UserPet> userPetList = userPetRepository.findAll();
        assertThat(userPetList).hasSize(databaseSizeBeforeCreate + 1);
        UserPet testUserPet = userPetList.get(userPetList.size() - 1);
        assertThat(testUserPet.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testUserPet.getUserStatus()).isEqualTo(DEFAULT_USER_STATUS);
    }

    @Test
    @Transactional
    void createUserPetWithExistingId() throws Exception {
        // Create the UserPet with an existing ID
        userPet.setId(1L);

        int databaseSizeBeforeCreate = userPetRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserPetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userPet)))
            .andExpect(status().isBadRequest());

        // Validate the UserPet in the database
        List<UserPet> userPetList = userPetRepository.findAll();
        assertThat(userPetList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUserStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = userPetRepository.findAll().size();
        // set the field null
        userPet.setUserStatus(null);

        // Create the UserPet, which fails.

        restUserPetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userPet)))
            .andExpect(status().isBadRequest());

        List<UserPet> userPetList = userPetRepository.findAll();
        assertThat(userPetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUserPets() throws Exception {
        // Initialize the database
        userPetRepository.saveAndFlush(userPet);

        // Get all the userPetList
        restUserPetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userPet.getId().intValue())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].userStatus").value(hasItem(DEFAULT_USER_STATUS)));
    }

    @Test
    @Transactional
    void getUserPet() throws Exception {
        // Initialize the database
        userPetRepository.saveAndFlush(userPet);

        // Get the userPet
        restUserPetMockMvc
            .perform(get(ENTITY_API_URL_ID, userPet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userPet.getId().intValue()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.userStatus").value(DEFAULT_USER_STATUS));
    }

    @Test
    @Transactional
    void getNonExistingUserPet() throws Exception {
        // Get the userPet
        restUserPetMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUserPet() throws Exception {
        // Initialize the database
        userPetRepository.saveAndFlush(userPet);

        int databaseSizeBeforeUpdate = userPetRepository.findAll().size();

        // Update the userPet
        UserPet updatedUserPet = userPetRepository.findById(userPet.getId()).get();
        // Disconnect from session so that the updates on updatedUserPet are not directly saved in db
        em.detach(updatedUserPet);
        updatedUserPet.phone(UPDATED_PHONE).userStatus(UPDATED_USER_STATUS);

        restUserPetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUserPet.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedUserPet))
            )
            .andExpect(status().isOk());

        // Validate the UserPet in the database
        List<UserPet> userPetList = userPetRepository.findAll();
        assertThat(userPetList).hasSize(databaseSizeBeforeUpdate);
        UserPet testUserPet = userPetList.get(userPetList.size() - 1);
        assertThat(testUserPet.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testUserPet.getUserStatus()).isEqualTo(UPDATED_USER_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingUserPet() throws Exception {
        int databaseSizeBeforeUpdate = userPetRepository.findAll().size();
        userPet.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserPetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userPet.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userPet))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserPet in the database
        List<UserPet> userPetList = userPetRepository.findAll();
        assertThat(userPetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserPet() throws Exception {
        int databaseSizeBeforeUpdate = userPetRepository.findAll().size();
        userPet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserPetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userPet))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserPet in the database
        List<UserPet> userPetList = userPetRepository.findAll();
        assertThat(userPetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserPet() throws Exception {
        int databaseSizeBeforeUpdate = userPetRepository.findAll().size();
        userPet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserPetMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userPet)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserPet in the database
        List<UserPet> userPetList = userPetRepository.findAll();
        assertThat(userPetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserPetWithPatch() throws Exception {
        // Initialize the database
        userPetRepository.saveAndFlush(userPet);

        int databaseSizeBeforeUpdate = userPetRepository.findAll().size();

        // Update the userPet using partial update
        UserPet partialUpdatedUserPet = new UserPet();
        partialUpdatedUserPet.setId(userPet.getId());

        restUserPetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserPet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserPet))
            )
            .andExpect(status().isOk());

        // Validate the UserPet in the database
        List<UserPet> userPetList = userPetRepository.findAll();
        assertThat(userPetList).hasSize(databaseSizeBeforeUpdate);
        UserPet testUserPet = userPetList.get(userPetList.size() - 1);
        assertThat(testUserPet.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testUserPet.getUserStatus()).isEqualTo(DEFAULT_USER_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateUserPetWithPatch() throws Exception {
        // Initialize the database
        userPetRepository.saveAndFlush(userPet);

        int databaseSizeBeforeUpdate = userPetRepository.findAll().size();

        // Update the userPet using partial update
        UserPet partialUpdatedUserPet = new UserPet();
        partialUpdatedUserPet.setId(userPet.getId());

        partialUpdatedUserPet.phone(UPDATED_PHONE).userStatus(UPDATED_USER_STATUS);

        restUserPetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserPet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserPet))
            )
            .andExpect(status().isOk());

        // Validate the UserPet in the database
        List<UserPet> userPetList = userPetRepository.findAll();
        assertThat(userPetList).hasSize(databaseSizeBeforeUpdate);
        UserPet testUserPet = userPetList.get(userPetList.size() - 1);
        assertThat(testUserPet.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testUserPet.getUserStatus()).isEqualTo(UPDATED_USER_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingUserPet() throws Exception {
        int databaseSizeBeforeUpdate = userPetRepository.findAll().size();
        userPet.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserPetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userPet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userPet))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserPet in the database
        List<UserPet> userPetList = userPetRepository.findAll();
        assertThat(userPetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserPet() throws Exception {
        int databaseSizeBeforeUpdate = userPetRepository.findAll().size();
        userPet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserPetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userPet))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserPet in the database
        List<UserPet> userPetList = userPetRepository.findAll();
        assertThat(userPetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserPet() throws Exception {
        int databaseSizeBeforeUpdate = userPetRepository.findAll().size();
        userPet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserPetMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(userPet)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserPet in the database
        List<UserPet> userPetList = userPetRepository.findAll();
        assertThat(userPetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserPet() throws Exception {
        // Initialize the database
        userPetRepository.saveAndFlush(userPet);

        int databaseSizeBeforeDelete = userPetRepository.findAll().size();

        // Delete the userPet
        restUserPetMockMvc
            .perform(delete(ENTITY_API_URL_ID, userPet.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserPet> userPetList = userPetRepository.findAll();
        assertThat(userPetList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
