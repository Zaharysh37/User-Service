package com.innowise.userservice.api.dto.userdto;

import com.innowise.userservice.api.dto.cardinfodto.GetCardInfoDto;
import java.io.Serializable;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class GetUserDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String surname;
    private LocalDate birthDate;
    private String email;
    private List<GetCardInfoDto> cards;
}
