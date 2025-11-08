package com.innowise.userservice.security;

import com.innowise.userservice.core.dao.*;
import com.innowise.userservice.core.entity.*;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Component("securityHelper")
@RequiredArgsConstructor
public class SecurityHelper {

    private final UserRepository userRepository;
    private final CardInfoRepository cardInfoRepository;

    public boolean isOwner(Long id) {
        UUID subFromToken = getSubFromToken();
        if (subFromToken == null) {
            return false;
        }

        User userFromDb = userRepository.findById(id).orElse(null);
        if (userFromDb == null) {
            return false;
        }

        return userFromDb.getSub() != null && userFromDb.getSub().equals(subFromToken);
    }

    public boolean isCardOwner(Long cardId) {
        UUID subFromToken = getSubFromToken();
        if (subFromToken == null) return false;

        CardInfo card = cardInfoRepository.findById(cardId).orElse(null);
        if (card == null) return false;

        User owner = card.getUser();
        if (owner == null || owner.getSub() == null) return false;

        return owner.getSub().equals(subFromToken);
    }

    private UUID getSubFromToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken token) {
            String sub = token.getToken().getSubject();
            if (sub != null) {
                return UUID.fromString(sub);
            }
        }
        return null;
    }
}
