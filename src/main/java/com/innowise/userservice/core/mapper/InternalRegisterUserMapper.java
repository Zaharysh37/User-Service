package com.innowise.userservice.core.mapper;

import com.innowise.userservice.api.dto.InternalRegisterUserDto;
import com.innowise.userservice.core.entity.User;
import org.mapstruct.Mapper;

@Mapper(config = BaseMapper.class)
public interface InternalRegisterUserMapper extends BaseMapper<User, InternalRegisterUserDto> {
}
