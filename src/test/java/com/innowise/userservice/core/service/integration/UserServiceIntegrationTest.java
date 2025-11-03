package com.innowise.userservice.core.service.integration;

import com.innowise.userservice.api.dto.userdto.CreateUserDto;
import com.innowise.userservice.api.dto.userdto.GetUserDto;
import com.innowise.userservice.core.dao.UserRepository;
import com.innowise.userservice.core.exception.ResourceNotFoundException;
import com.innowise.userservice.core.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Objects;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class UserServiceIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    @AfterEach
    void cleanup() {
        userRepository.deleteAll();
        Objects.requireNonNull(cacheManager.getCache("users")).clear();
    }

    private CreateUserDto createTestUserDto() {
        CreateUserDto dto = new CreateUserDto();
        dto.setName("Ivan");
        dto.setSurname("Ivanov");
        dto.setEmail("ivan@mail.com");
        return dto;
    }

    @Test
    void test_createUser_Success() {
        CreateUserDto dto = createTestUserDto();

        GetUserDto createdUser = userService.createUser(dto);

        assertNotNull(createdUser.getId());
        assertEquals("Ivan", createdUser.getName());

        assertTrue(userRepository.findById(createdUser.getId()).isPresent());
    }

    @Test
    void test_getUserById_ThrowsException_IfNotFound() {
        assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUserById(99L);
        });
    }

    @Test
    void test_Cache_FullLifecycle_Get_Update_Delete() {
        GetUserDto createdUser = userService.createUser(createTestUserDto());
        Long userId = createdUser.getId();

        assertNull(getCacheValue(userId));

        GetUserDto userFromDb = userService.getUserById(userId);
        assertEquals("Ivan", userFromDb.getName());

        assertNotNull(getCacheValue(userId));
        assertEquals("Ivan", ((GetUserDto) getCacheValue(userId)).getName());

        GetUserDto userFromCache = userService.getUserById(userId);
        assertEquals("Ivan", userFromCache.getName());

        CreateUserDto updateDto = createTestUserDto();
        updateDto.setName("Petr");

        userService.updateUser(userId, updateDto);

        assertNotNull(getCacheValue(userId));
        assertEquals("Petr", ((GetUserDto) getCacheValue(userId)).getName());

        userService.deleteUser(userId);

        assertNull(getCacheValue(userId));

        assertFalse(userRepository.findById(userId).isPresent());
    }

    private Object getCacheValue(Long id) {
        Cache.ValueWrapper valueWrapper = cacheManager.getCache("users").get(id);
        return (valueWrapper != null) ? valueWrapper.get() : null;
    }
}