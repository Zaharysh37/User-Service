package com.innowise.userservice.api.controller;

import com.innowise.userservice.api.dto.userdto.CreateUserDto;
import com.innowise.userservice.api.dto.userdto.GetUserDto;
import com.innowise.userservice.core.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<GetUserDto> createUser(@Valid @RequestBody CreateUserDto dto) {
        GetUserDto createdUser = userService.createUser(dto);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<GetUserDto>> getAllUsers(Pageable pageable) {
        Page<GetUserDto> users = userService.getAllUsers(pageable);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetUserDto> getUserById(@PathVariable Long id) {
        GetUserDto user = userService.getUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/email")
    public ResponseEntity<GetUserDto> getUserByEmail(@RequestParam String email) {
        GetUserDto user = userService.getUserByEmail(email);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/by-surname")
    public ResponseEntity<Page<GetUserDto>> getUserBySurname(@RequestParam String letter, Pageable pageable) {
        Page<GetUserDto> users = userService.getUserByFirstLettersOfSurname(letter, pageable);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/card-number")
    public ResponseEntity<GetUserDto> getUserByCardNumber(@RequestParam String number) {
        GetUserDto user = userService.getUserByCardNumber(number);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GetUserDto> updateUser(@PathVariable Long id,
                                                 @Valid @RequestBody CreateUserDto dto) {
        GetUserDto updatedUser = userService.updateUser(id, dto);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }
}
