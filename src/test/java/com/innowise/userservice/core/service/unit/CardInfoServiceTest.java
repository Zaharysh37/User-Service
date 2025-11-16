package com.innowise.userservice.core.service.unit;

import com.innowise.userservice.api.dto.cardinfodto.CreateCardInfoDto;
import com.innowise.userservice.api.dto.cardinfodto.GetCardInfoDto;
import com.innowise.userservice.core.dao.CardInfoRepository;
import com.innowise.userservice.core.dao.UserRepository;
import com.innowise.userservice.core.entity.CardInfo;
import com.innowise.userservice.core.entity.User;
import com.innowise.userservice.core.exception.ResourceNotFoundException;
import com.innowise.userservice.core.mapper.cardinfomapper.CreateCardInfoMapper;
import com.innowise.userservice.core.mapper.cardinfomapper.GetCardInfoMapper;
import com.innowise.userservice.core.service.CardInfoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardInfoServiceTest {

    @Mock
    private CardInfoRepository cardInfoRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private GetCardInfoMapper getCardInfoMapper;
    @Mock
    private CreateCardInfoMapper createCardInfoMapper;

    @InjectMocks
    private CardInfoService cardInfoService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void test_createCardInfos_Success() {
        Long userId = 1L;
        CreateCardInfoDto createDto = new CreateCardInfoDto();
        User existingUser = new User();
        existingUser.setId(userId);

        CardInfo cardToSave = new CardInfo();
        CardInfo savedCard = new CardInfo();
        savedCard.setId(10L);

        GetCardInfoDto expectedDto = new GetCardInfoDto();
        expectedDto.setId(10L);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(createCardInfoMapper.toEntity(createDto)).thenReturn(cardToSave);
        when(cardInfoRepository.save(cardToSave)).thenReturn(savedCard);
        when(getCardInfoMapper.toDto(savedCard)).thenReturn(expectedDto);

        GetCardInfoDto result = cardInfoService.createCardInfos(userId, createDto);

        assertNotNull(result);
        assertEquals(10L, result.getId());

        assertEquals(existingUser, cardToSave.getUser());

        verify(userRepository, times(1)).findById(userId);
        verify(cardInfoRepository, times(1)).save(cardToSave);
    }

    @Test
    void test_createCardInfos_UserNotFound_ThrowsException() {
        Long userId = 99L;
        CreateCardInfoDto createDto = new CreateCardInfoDto();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            cardInfoService.createCardInfos(userId, createDto);
        });

        verify(cardInfoRepository, never()).save(any());
    }

    @Test
    void test_getCardInfoById_Success() {
        CardInfo cardEntity = new CardInfo();
        cardEntity.setId(1L);
        GetCardInfoDto expectedDto = new GetCardInfoDto();
        expectedDto.setId(1L);

        when(cardInfoRepository.findById(1L)).thenReturn(Optional.of(cardEntity));
        when(getCardInfoMapper.toDto(cardEntity)).thenReturn(expectedDto);

        GetCardInfoDto result = cardInfoService.getCardInfoById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void test_getCardInfoById_NotFound_ThrowsException() {
        when(cardInfoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            cardInfoService.getCardInfoById(99L);
        });
    }

    @Test
    void test_getAllCardInfos_Success() {
        Pageable pageable = Pageable.unpaged();
        CardInfo card1 = new CardInfo();
        Page<CardInfo> cardPage = new PageImpl<>(List.of(card1));

        when(cardInfoRepository.findAll(pageable)).thenReturn(cardPage);

        Page<GetCardInfoDto> result = cardInfoService.getAllCardInfos(pageable);

        assertEquals(1, result.getTotalElements());
        verify(getCardInfoMapper, times(1)).toDto(card1);
    }

    @Test
    void test_deleteCardInfo_Success() {
        Long cardId = 1L;
        CardInfo existingCard = new CardInfo();
        existingCard.setId(cardId);

        when(cardInfoRepository.findById(cardId)).thenReturn(Optional.of(existingCard));

        cardInfoService.deleteCardInfo(cardId);

        verify(cardInfoRepository, times(1)).findById(cardId);
        verify(cardInfoRepository, times(1)).delete(existingCard);
    }

    @Test
    void test_cleanupExpiredCards_Success() {
        when(cardInfoRepository.deleteAllExpiredCards(anyString())).thenReturn(5);

        int deletedCount = cardInfoService.cleanupExpiredCards();

        assertEquals(5, deletedCount);
        verify(cardInfoRepository, times(1)).deleteAllExpiredCards(anyString());
    }
}