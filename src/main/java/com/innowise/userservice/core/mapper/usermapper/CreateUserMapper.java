package com.innowise.userservice.core.mapper.usermapper;

import com.innowise.userservice.api.dto.userdto.CreateUserDto;
import com.innowise.userservice.core.entity.User;
import com.innowise.userservice.core.mapper.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(config = BaseMapper.class)
public interface CreateUserMapper extends BaseMapper<User, CreateUserDto> {
}
