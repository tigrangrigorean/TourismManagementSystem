package com.tourism.model.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SignUp {


    @NotNull(message = "Name cannot be null")
    @Pattern(regexp = "[A-Z][a-z]+", message = "First name must start with a capital letter followed by lowercase letters")
    private String name;


    @NotNull(message = "Surname cannot be null")
    @Pattern(regexp = "[A-Z][a-z]+", message = "Last name must start with a capital letter followed by lowercase letters")
    private String surName;


    @Email(message = "Email should be valid and can`t be empty")
    private String email;

    private String phone;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+])[a-zA-Z0-9!@#$%^&*()_+]{8,20}$"
            , message = "The password must contain uppercase and lowercase letters, mathematical symbols and numbers.")
    private String password;


}
