package com.innowise.userservice.core.service;

import com.innowise.userservice.api.dto.userdto.CreateUserDto;
import com.innowise.userservice.api.dto.userdto.GetUserDto;
import com.innowise.userservice.core.dao.CardInfoRepository;
import com.innowise.userservice.core.dao.UserRepository;
import com.innowise.userservice.core.entity.User;
import com.innowise.userservice.core.exception.ResourceNotFoundException;
import com.innowise.userservice.core.mapper.usermapper.CreateUserMapper;
import com.innowise.userservice.core.mapper.usermapper.GetUserMapper;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final CreateUserMapper createUserMapper;
    private final GetUserMapper getUserMapper;
    private final CardInfoRepository cardInfoRepository;

    @Transactional
    public GetUserDto createUser(CreateUserDto dto) {
        User user = createUserMapper.toEntity(dto);
        User savedUser = userRepository.save(user);
        return getUserMapper.toDto(savedUser);
    }

    @Cacheable(value = "users", key = "#id")
    public GetUserDto getUserById(Long id) {
        User existingUser = findUserById(id);
        return getUserMapper.toDto(existingUser);
    }

    public GetUserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        return getUserMapper.toDto(user);
    }

    public Page<GetUserDto> getUserByFirstLettersOfSurname(String letter, Pageable pageable) {
        Page<User> users = userRepository.findBySurnameStartsWith(letter, pageable);
        return users.map(getUserMapper::toDto);
    }

    public Page<GetUserDto> getAllUsers(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return users.map(getUserMapper::toDto);
    }

    @Transactional
    @CachePut(value = "users", key = "#id")
    public GetUserDto updateUser(Long id, CreateUserDto dto) {
        User existingUser = findUserById(id);

        createUserMapper.merge(existingUser, dto);
        User updateUser = userRepository.save(existingUser);

        return getUserMapper.toDto(updateUser);
    }

    @Transactional
    @CacheEvict(value = "users", key = "#id")
    public void deleteUser(Long id) {
        User existingUser = findUserById(id);
        userRepository.delete(existingUser);
    }

    private User findUserById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    public GetUserDto getUserByCardNumber(String cardNumber) {
        User user = cardInfoRepository.findUserByCardNumber(cardNumber)
            .orElseThrow(() -> new ResourceNotFoundException("User not found for card: " + cardNumber));
        return getUserMapper.toDto(user);
    }
}
