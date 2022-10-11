package mx.conacyt.crip.ejemplos.openapi.repository;

import mx.conacyt.crip.ejemplos.openapi.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {}
