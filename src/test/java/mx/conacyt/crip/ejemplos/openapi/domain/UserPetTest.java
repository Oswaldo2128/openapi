package mx.conacyt.crip.ejemplos.openapi.domain;

import static org.assertj.core.api.Assertions.assertThat;

import mx.conacyt.crip.ejemplos.openapi.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserPetTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserPet.class);
        UserPet userPet1 = new UserPet();
        userPet1.setId(1L);
        UserPet userPet2 = new UserPet();
        userPet2.setId(userPet1.getId());
        assertThat(userPet1).isEqualTo(userPet2);
        userPet2.setId(2L);
        assertThat(userPet1).isNotEqualTo(userPet2);
        userPet1.setId(null);
        assertThat(userPet1).isNotEqualTo(userPet2);
    }
}
