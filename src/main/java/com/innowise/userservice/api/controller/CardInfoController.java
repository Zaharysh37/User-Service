package com.innowise.userservice.api.controller;

import com.innowise.userservice.api.dto.cardinfodto.CreateCardInfoDto;
import com.innowise.userservice.api.dto.cardinfodto.GetCardInfoDto;
import com.innowise.userservice.core.service.CardInfoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.*;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CardInfoController {

    private final CardInfoService cardInfoService;

    @PostMapping("/users/{userId}/cards")
    @PreAuthorize("hasRole('ADMIN') or @securityHelper.isOwner(#userId)")
    public ResponseEntity<GetCardInfoDto> createCardForUser(
        @PathVariable Long userId,
        @Valid @RequestBody CreateCardInfoDto createCardInfoDto) {

        GetCardInfoDto createdCard = cardInfoService.createCardInfos(userId, createCardInfoDto);
        return new ResponseEntity<>(createdCard, HttpStatus.CREATED);
    }

    @GetMapping("/cards/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityHelper.isCardOwner(#id)")
    public ResponseEntity<GetCardInfoDto> getCardById(@PathVariable Long id) {
        GetCardInfoDto card = cardInfoService.getCardInfoById(id);
        return new ResponseEntity<>(card, HttpStatus.OK);
    }

    @GetMapping("/cards")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<GetCardInfoDto>> getAllCards(Pageable pageable) {
        Page<GetCardInfoDto> cards = cardInfoService.getAllCardInfos(pageable);
        return new ResponseEntity<>(cards, HttpStatus.OK);
    }

    @DeleteMapping("/cards/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityHelper.isCardOwner(#id)")
    public ResponseEntity<Void> deleteCard(@PathVariable Long id) {
        cardInfoService.deleteCardInfo(id);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }
}