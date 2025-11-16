package com.innowise.userservice.api.dto.userdto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.UUID;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CreateUserDto {

    @NotNull
    private UUID sub;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100)
    private String name;

    @NotBlank(message = "Surname is required")
    @Size(min = 2, max = 100)
    private String surname;

    @Past(message = "Birth date must be in the past")
    private LocalDate birthDate;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
}
