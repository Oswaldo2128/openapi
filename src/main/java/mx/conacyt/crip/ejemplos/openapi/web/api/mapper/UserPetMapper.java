package mx.conacyt.crip.ejemplos.openapi.web.api.mapper;

import mx.conacyt.crip.ejemplos.openapi.domain.UserPet;
import mx.conacyt.crip.ejemplos.openapi.service.api.dto.UserDto;
import mx.conacyt.crip.ejemplos.openapi.service.mapper.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingInheritanceStrategy;

@Mapper(componentModel = "spring", mappingInheritanceStrategy = MappingInheritanceStrategy.AUTO_INHERIT_ALL_FROM_CONFIG)
public interface UserPetMapper extends EntityMapper<UserDto, UserPet> {
    @Mapping(source = "user.login", target = "username")
    @Mapping(source = "user.firstName", target = "firstName")
    @Mapping(source = "user.lastName", target = "lastName")
    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "user.password", target = "password")
    UserDto toDto(UserPet entity);

    @Mapping(source = "username", target = "user.login")
    @Mapping(source = "firstName", target = "user.firstName")
    @Mapping(source = "lastName", target = "user.lastName")
    @Mapping(source = "email", target = "user.email")
    @Mapping(source = "password", target = "user.password")
    UserPet toEntity(UserDto dto);
}
