package com.innowise.userservice.api.dto.cardinfodto;

import lombok.Data;

@Data
public class GetCardInfoDto {
    private Long id;
    private String number;
    private String expirationDate;
}
