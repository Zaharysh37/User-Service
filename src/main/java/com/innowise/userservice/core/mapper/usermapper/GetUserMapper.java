package com.innowise.userservice.core.mapper.usermapper;

import com.innowise.userservice.api.dto.userdto.GetUserDto;
import com.innowise.userservice.core.entity.User;
import com.innowise.userservice.core.mapper.BaseMapper;
import com.innowise.userservice.core.mapper.cardinfomapper.GetCardInfoMapper;
import org.mapstruct.Mapper;

@Mapper(config = BaseMapper.class, uses = {GetCardInfoMapper.class})
public interface GetUserMapper extends BaseMapper<User, GetUserDto> {
}
