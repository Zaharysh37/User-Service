package com.innowise.userservice.core.service;

import com.innowise.userservice.api.dto.cardinfodto.CreateCardInfoDto;
import com.innowise.userservice.api.dto.cardinfodto.GetCardInfoDto;
import com.innowise.userservice.core.dao.CardInfoRepository;
import com.innowise.userservice.core.dao.UserRepository;
import com.innowise.userservice.core.entity.CardInfo;
import com.innowise.userservice.core.entity.User;
import com.innowise.userservice.core.mapper.cardinfomapper.CreateCardInfoMapper;
import com.innowise.userservice.core.mapper.cardinfomapper.GetCardInfoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardInfoService {
    private final CardInfoRepository cardInfoRepository;
    private final UserRepository userRepository;
    private final GetCardInfoMapper getCardInfoMapper;
    private final CreateCardInfoMapper createCardInfoMapper;

    @Transactional
    public GetCardInfoDto createCardInfos(Long userId, CreateCardInfoDto createCardInfoDto) {
        User user = userRepository.findById(userId)
            .orElse(null); //Exception

        CardInfo cardInfo = createCardInfoMapper.toEntity(createCardInfoDto);
        cardInfo.setUser(user);

        CardInfo savedCard = cardInfoRepository.save(cardInfo);
        return getCardInfoMapper.toDto(savedCard);
    }

    public GetCardInfoDto getCardInfoById(Long id) {
        CardInfo existingCardInfo = findCardInfoById(id);
        return getCardInfoMapper.toDto(existingCardInfo);
    }

    public Page<GetCardInfoDto> getAllCardInfos(Pageable pageable) {
        Page<CardInfo> cardInfos = cardInfoRepository.findAll(pageable);
        return cardInfos.map(getCardInfoMapper::toDto);
    }

    @Transactional
    public void deleteCardInfos(Long userId) {
        CardInfo cardInfo = findCardInfoById(userId);
        cardInfoRepository.delete(cardInfo);
    }

    private CardInfo findCardInfoById(Long id) {
        return cardInfoRepository.findById(id)
            .orElse(null); //Exception
    }
}
