package mx.conacyt.crip.ejemplos.openapi.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import mx.conacyt.crip.ejemplos.openapi.domain.User;
import mx.conacyt.crip.ejemplos.openapi.domain.UserPet;
import mx.conacyt.crip.ejemplos.openapi.repository.UserPetRepository;
import mx.conacyt.crip.ejemplos.openapi.repository.UserRepository;
import mx.conacyt.crip.ejemplos.openapi.service.UserPetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link UserPet}.
 */
@Service
@Transactional
public class UserPetServiceImpl implements UserPetService {

    private final Logger log = LoggerFactory.getLogger(UserPetServiceImpl.class);

    private final UserPetRepository userPetRepository;
    private final UserRepository userRepository;

    public UserPetServiceImpl(UserPetRepository userPetRepository, UserRepository userRepository) {
        this.userPetRepository = userPetRepository;
        this.userRepository = userRepository;
    }

    @Override
    public UserPet save(UserPet userPet) {
        log.debug("Request to save UserPet : {}", userPet);
        return userPetRepository.save(userPet);
    }

    @Override
    public UserPet update(UserPet userPet) {
        log.debug("Request to save UserPet : {}", userPet);
        return userPetRepository.save(userPet);
    }

    @Override
    public Optional<UserPet> partialUpdate(UserPet userPet) {
        log.debug("Request to partially update UserPet : {}", userPet);

        return userPetRepository
            .findById(userPet.getId())
            .map(existingUserPet -> {
                if (userPet.getPhone() != null) {
                    existingUserPet.setPhone(userPet.getPhone());
                }
                if (userPet.getUserStatus() != null) {
                    existingUserPet.setUserStatus(userPet.getUserStatus());
                }

                return existingUserPet;
            })
            .map(userPetRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserPet> findAll() {
        log.debug("Request to get all UserPets");
        return userPetRepository.findAll();
    }

    /**
     *  Get all the userPets where Customer is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<UserPet> findAllWhereCustomerIsNull() {
        log.debug("Request to get all userPets where Customer is null");
        return StreamSupport
            .stream(userPetRepository.findAll().spliterator(), false)
            .filter(userPet -> userPet.getCustomer() == null)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserPet> findOne(Long id) {
        log.debug("Request to get UserPet : {}", id);
        return userPetRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserPet : {}", id);
        userPetRepository.deleteById(id);
    }

    @Override
    public Optional<UserPet> findByUserName(String username) {
        Optional<User> userOpt = userRepository.findOneByLogin(username);
        if (userOpt.isPresent()) {
            Optional<UserPet> userPetOpt = userPetRepository.findByUser(userOpt.get());
            if (userPetOpt.isPresent()) {
                return userPetOpt;
            } else {
                UserPet userPet = new UserPet();
                userPet.setUser(userOpt.get());
                userPet.setUserStatus(0);
                userPetRepository.save(userPet);
                return Optional.of(userPet);
            }
        }
        return Optional.empty();
    }
}
