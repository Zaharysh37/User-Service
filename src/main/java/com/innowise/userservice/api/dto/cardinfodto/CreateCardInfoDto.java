package com.innowise.userservice.api.dto.cardinfodto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CreateCardInfoDto {
    @NotBlank(message = "Card number is required")
    private String number;

    @NotBlank(message = "Expiration date is required")
    @Pattern(regexp = "^(0[1-9]|1[0-2])/([0-9]{2})$", message = "Invalid format. Must be MM/YY")
    private String expirationDate;
}
