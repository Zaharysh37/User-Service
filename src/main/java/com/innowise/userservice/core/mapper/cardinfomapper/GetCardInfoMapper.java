package com.innowise.userservice.core.mapper.cardinfomapper;

import com.innowise.userservice.api.dto.cardinfodto.GetCardInfoDto;
import com.innowise.userservice.core.entity.CardInfo;
import com.innowise.userservice.core.mapper.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(config = BaseMapper.class)
public interface GetCardInfoMapper extends BaseMapper<CardInfo, GetCardInfoDto> {
}
