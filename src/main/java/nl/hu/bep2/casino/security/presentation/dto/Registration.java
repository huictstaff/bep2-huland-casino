package nl.hu.bep2.casino.security.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class Registration {
    @NotBlank
    public String username;

    @Size(min = 5)
    public String password;

    @NotBlank
    public String firstName;

    @NotBlank
    public String lastName;
}
