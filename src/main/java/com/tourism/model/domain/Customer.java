package com.tourism.model.domain;

import com.tourism.model.dto.SignUp;
import com.tourism.model.vo.Role;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.validator.constraints.Email;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Name cannot be null")
    @Pattern(regexp = "[A-Z][a-z]+", message = "First name must start with a capital letter followed by lowercase letters")
    @Column(name = "first_name", nullable = false, length = 20)
    private String name;

    @NotNull(message = "Last name cannot be null")
    @Pattern(regexp = "[A-Z][a-z]+", message = "Last name must start with a capital letter followed by lowercase letters")
    @Column(name = "sur_name", nullable = false, length = 20)
    private String surname;

    @Column(name = "email", nullable = false, unique = true)
    @Email(message = "Email should be valid and can`t be empty")
    private String email;


    private String phone;

    @NotNull(message = "Password should be valid and can`t be empty")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+])[a-zA-Z0-9!@#$%^&*()_+]{8,20}$"
            , message = "The password must contain uppercase and lowercase letters, mathematical symbols and numbers.")
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "verify_mail")
    private Boolean verifyMail;

    @Pattern(regexp = "^[1-9][0-9]{5}$")
    @Column(name = "pin")
    private String pin;

    @Column(name = "money")
    private Double money;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToMany
    @JoinTable(
            name = "customer_tour",
            joinColumns = @JoinColumn(name = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "tour_id")
    )
    private List<Tour> tours;



    public Customer(SignUp signUp) {
        this.name = signUp.getName();
        this.surname = signUp.getSurName();
        this.email = signUp.getEmail();
        this.password = signUp.getPassword();
        this.money = 0.;
    }
}
