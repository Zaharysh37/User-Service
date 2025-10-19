package com.innowise.userservice.core.mapper.cardinfomapper;

import com.innowise.userservice.api.dto.cardinfodto.CreateCardInfoDto;
import com.innowise.userservice.core.entity.CardInfo;
import com.innowise.userservice.core.mapper.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(config = BaseMapper.class)
public interface CreateCardInfoMapper extends BaseMapper<CardInfo, CreateCardInfoDto> {
}
