package com.innowise.userservice.core.service.integration;

import com.innowise.userservice.api.dto.cardinfodto.CreateCardInfoDto;
import com.innowise.userservice.api.dto.cardinfodto.GetCardInfoDto;
import com.innowise.userservice.core.dao.CardInfoRepository;
import com.innowise.userservice.core.dao.UserRepository;
import com.innowise.userservice.core.entity.User;
import com.innowise.userservice.core.exception.ResourceNotFoundException;
import com.innowise.userservice.core.service.CardInfoService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class CardInfoServiceIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private CardInfoService cardInfoService;
    @Autowired
    private CardInfoRepository cardInfoRepository;
    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        cardInfoRepository.deleteAll();
        userRepository.deleteAll();

        User user = new User();
        user.setName("Test");
        user.setSurname("User");
        user.setEmail("testuser@mail.com");
        testUser = userRepository.save(user);
    }

    @AfterEach
    void tearDown() {
        cardInfoRepository.deleteAll();
        userRepository.deleteAll();
    }

    private CreateCardInfoDto createTestCardDto() {
        CreateCardInfoDto dto = new CreateCardInfoDto();
        dto.setNumber("1234-5678-8765-4321");
        dto.setExpirationDate("10/30");
        return dto;
    }

    @Test
    void test_createCardInfos_Success() {
        CreateCardInfoDto dto = createTestCardDto();
        Long userId = testUser.getId();

        GetCardInfoDto createdCard = cardInfoService.createCardInfos(userId, dto);

        assertNotNull(createdCard.getId());
        assertEquals("1234-5678-8765-4321", createdCard.getNumber());

        var cardFromDb = cardInfoRepository.findById(createdCard.getId());
        assertTrue(cardFromDb.isPresent());
        assertEquals(userId, cardFromDb.get().getUser().getId());
    }

    @Test
    void test_createCardInfos_UserNotFound_ThrowsException() {
        CreateCardInfoDto dto = createTestCardDto();
        Long nonExistentUserId = 999L;

        assertThrows(ResourceNotFoundException.class, () -> {
            cardInfoService.createCardInfos(nonExistentUserId, dto);
        });
    }

    @Test
    void test_deleteCardInfo_Success() {
        GetCardInfoDto createdCard = cardInfoService.createCardInfos(testUser.getId(), createTestCardDto());
        Long cardId = createdCard.getId();

        assertTrue(cardInfoRepository.findById(cardId).isPresent());

        cardInfoService.deleteCardInfo(cardId);

        assertFalse(cardInfoRepository.findById(cardId).isPresent());

        assertTrue(userRepository.findById(testUser.getId()).isPresent());
    }
}