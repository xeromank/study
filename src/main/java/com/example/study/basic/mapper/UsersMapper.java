package com.example.study.basic.mapper;

import com.example.study.basic.domain.Users;
import com.example.study.basic.mapper.qualifier.SignInPwQualifier;
import com.example.study.common.dto.users.SignWithRequest;
import com.example.study.common.dto.users.SignWithResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    uses = {SignInPwQualifier.class})
public interface UsersMapper {

    UsersMapper INSTANCE = Mappers.getMapper(UsersMapper.class);

    static String regEx = "(\\d{3})(\\d{3,4})(\\d{4})";

    @Mapping(target = "signInCount", expression = "java(0L)")
    @Mapping(target = "errorCount", expression = "java(0)")
    @Mapping(source = "signInPw", target = "signInPw", qualifiedByName = "encodeSignInPw")
    Users signWithToUsers(SignWithRequest request);

    SignWithResponse toSignWithResponse(Users users);

//    @Mapping(target = "userPhoneNumber", expression = "java(com.aceent.holdem.security.EncryptService.decryptPhoneNumber(users.getUserPhoneNumber()).replaceAll(regEx, \"$1-$2-$3\"))")
//    UserDto convertUserDto(Users users);
}
