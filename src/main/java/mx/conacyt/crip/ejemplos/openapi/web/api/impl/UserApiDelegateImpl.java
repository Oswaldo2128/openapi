package mx.conacyt.crip.ejemplos.openapi.web.api.impl;

import java.util.Optional;
import mx.conacyt.crip.ejemplos.openapi.domain.UserPet;
import mx.conacyt.crip.ejemplos.openapi.service.UserPetService;
import mx.conacyt.crip.ejemplos.openapi.service.api.dto.UserDto;
import mx.conacyt.crip.ejemplos.openapi.web.api.UserApiDelegate;
import mx.conacyt.crip.ejemplos.openapi.web.api.mapper.UserPetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserApiDelegateImpl implements UserApiDelegate {

    @Autowired
    private UserPetService userPetService;

    @Autowired
    private UserPetMapper userPetMapper;

    @Override
    public ResponseEntity<UserDto> getUserByName(String username) {
        Optional<UserPet> userOtp = userPetService.findByUserName(username);
        if (userOtp.isPresent()) {
            return ResponseEntity.ok(userPetMapper.toDto(userOtp.get()));
        }
        return ResponseEntity.notFound().build();
    }
}
