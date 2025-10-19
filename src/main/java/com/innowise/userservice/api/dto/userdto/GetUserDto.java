package com.innowise.userservice.api.dto.userdto;

import com.innowise.userservice.api.dto.cardinfodto.GetCardInfoDto;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class GetUserDto {
    private Long id;
    private String name;
    private String surname;
    private LocalDate birthDate;
    private String email;
    private List<GetCardInfoDto> cards;
}
