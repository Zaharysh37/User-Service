package com.innowise.userservice.api.dto.cardinfodto;

import java.io.Serializable;
import lombok.Data;

@Data
public class GetCardInfoDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String number;
    private String expirationDate;
}
