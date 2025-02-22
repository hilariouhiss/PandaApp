package me.lhy.pandaid.util;

import me.lhy.pandaid.domain.dto.UserDto;
import me.lhy.pandaid.domain.po.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface Converter {

    Converter INSTANCE = Mappers.getMapper(Converter.class);

    UserDto toUserDto(User user);

    User toUser(UserDto userDto);

}
