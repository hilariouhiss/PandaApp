package me.lhy.pandaid.util;

import me.lhy.pandaid.domain.dto.PandaDTO;
import me.lhy.pandaid.domain.dto.RegisterDTO;
import me.lhy.pandaid.domain.dto.UserDTO;
import me.lhy.pandaid.domain.po.Panda;
import me.lhy.pandaid.domain.po.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface Converter {

    Converter INSTANCE = Mappers.getMapper(Converter.class);

    UserDTO toUserDto(User user);

    User toUser(RegisterDTO dto);

    User toUser(UserDTO dto);

    Panda toPanda(PandaDTO dto);

    PandaDTO toPandaDto(Panda panda);
}
