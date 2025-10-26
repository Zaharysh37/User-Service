package com.innowise.userservice.core.service;

import com.innowise.userservice.api.dto.cardinfodto.CreateCardInfoDto;
import com.innowise.userservice.api.dto.cardinfodto.GetCardInfoDto;
import com.innowise.userservice.core.dao.CardInfoRepository;
import com.innowise.userservice.core.dao.UserRepository;
import com.innowise.userservice.core.entity.CardInfo;
import com.innowise.userservice.core.exception.ResourceNotFoundException;
import com.innowise.userservice.core.mapper.cardinfomapper.CreateCardInfoMapper;
import com.innowise.userservice.core.mapper.cardinfomapper.GetCardInfoMapper;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    private static final DateTimeFormatter EXPIRATION_FORMAT = DateTimeFormatter.ofPattern("MM/yy");

    @Transactional
    public GetCardInfoDto createCardInfos(Long userId, CreateCardInfoDto createCardInfoDto) {
        CardInfo cardInfo = createCardInfoMapper.toEntity(createCardInfoDto);
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
    public void deleteCardInfo(Long id) {
        CardInfo cardInfo = findCardInfoById(id);
        cardInfoRepository.delete(cardInfo);
    }

    private CardInfo findCardInfoById(Long id) {
        return cardInfoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("CardInfo not found with id: " + id));
    }

    @Transactional
    public int cleanupExpiredCards() {
        LocalDate lastDay = LocalDate.now().minusDays(1);
        String expiredDateString = lastDay.format(EXPIRATION_FORMAT);

        int deletedCount = cardInfoRepository.deleteAllExpiredCards(expiredDateString);

        return deletedCount;
    }
}
