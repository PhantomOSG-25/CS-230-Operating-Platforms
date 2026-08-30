package com.gamingroom.gameauth.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateGameUserRequest(
    @NotBlank String firstName, @NotBlank String lastName, @NotBlank @Email String email) {}
