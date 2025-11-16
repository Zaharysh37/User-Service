package com.innowise.userservice.core.service.unit;

import com.innowise.userservice.api.dto.userdto.CreateUserDto;
import com.innowise.userservice.api.dto.userdto.GetUserDto;
import com.innowise.userservice.core.dao.CardInfoRepository;
import com.innowise.userservice.core.dao.UserRepository;
import com.innowise.userservice.core.entity.User;
import com.innowise.userservice.core.exception.ResourceNotFoundException;
import com.innowise.userservice.core.mapper.usermapper.CreateUserMapper;
import com.innowise.userservice.core.mapper.usermapper.GetUserMapper;
import com.innowise.userservice.core.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private CreateUserMapper createUserMapper;
    @Mock
    private GetUserMapper getUserMapper;
    @Mock
    private CardInfoRepository cardInfoRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void test_createUser_Success() {
        CreateUserDto createDto = new CreateUserDto();
        User userEntity = new User();
        User savedUserEntity = new User();
        savedUserEntity.setId(1L);
        GetUserDto expectedDto = new GetUserDto();
        expectedDto.setId(1L);

        when(createUserMapper.toEntity(createDto)).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(savedUserEntity);
        when(getUserMapper.toDto(savedUserEntity)).thenReturn(expectedDto);

        GetUserDto result = userService.createUser(createDto);

        assertNotNull(result);
        assertEquals(expectedDto.getId(), result.getId());

        verify(createUserMapper, times(1)).toEntity(createDto);
        verify(userRepository, times(1)).save(userEntity);
        verify(getUserMapper, times(1)).toDto(savedUserEntity);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void test_getUserById_Success() {
        User userEntity = new User();
        userEntity.setId(1L);
        GetUserDto expectedDto = new GetUserDto();
        expectedDto.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(getUserMapper.toDto(userEntity)).thenReturn(expectedDto);

        GetUserDto result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void test_getUserById_NotFound_ThrowsException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUserById(99L);
        });

        verify(getUserMapper, never()).toDto(any());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void test_getUserByEmail_Success() {
        String email = "test@mail.com";
        User userEntity = new User();
        userEntity.setEmail(email);
        GetUserDto expectedDto = new GetUserDto();
        expectedDto.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userEntity));
        when(getUserMapper.toDto(userEntity)).thenReturn(expectedDto);

        GetUserDto result = userService.getUserByEmail(email);

        assertNotNull(result);
        assertEquals(email, result.getEmail());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void test_getUserByEmail_NotFound_ThrowsException() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUserByEmail("not@found.com");
        });
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void test_getAllUsers_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        User user1 = new User();
        Page<User> userPage = new PageImpl<>(List.of(user1), pageable, 1);


        when(userRepository.findAll(pageable)).thenReturn(userPage);

        Page<GetUserDto> result = userService.getAllUsers(pageable);

        assertEquals(1, result.getTotalElements());
        verify(getUserMapper, times(1)).toDto(user1);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void test_updateUser_Success() {
        Long userId = 1L;
        CreateUserDto updateDto = new CreateUserDto();
        updateDto.setName("NewName");

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setName("OldName");

        GetUserDto expectedDto = new GetUserDto();
        expectedDto.setId(userId);
        expectedDto.setName("NewName");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser); // save вернет обновленного
        when(getUserMapper.toDto(existingUser)).thenReturn(expectedDto);

        GetUserDto result = userService.updateUser(userId, updateDto);

        assertNotNull(result);
        assertEquals("NewName", result.getName());

        verify(createUserMapper, times(1)).merge(existingUser, updateDto);
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void test_deleteUser_Success() {
        Long userId = 1L;
        User existingUser = new User();
        existingUser.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        userService.deleteUser(userId);

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).delete(existingUser);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void test_getUserByCardNumber_Success() {
        String cardNumber = "1234-5678";
        User userEntity = new User();
        GetUserDto expectedDto = new GetUserDto();

        when(cardInfoRepository.findUserByCardNumber(cardNumber)).thenReturn(Optional.of(userEntity));
        when(getUserMapper.toDto(userEntity)).thenReturn(expectedDto);

        GetUserDto result = userService.getUserByCardNumber(cardNumber);

        assertNotNull(result);
        verify(cardInfoRepository, times(1)).findUserByCardNumber(cardNumber);
    }
}