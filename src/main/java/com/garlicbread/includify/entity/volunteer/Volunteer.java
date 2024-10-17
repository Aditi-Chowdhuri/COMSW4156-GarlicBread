package com.garlicbread.includify.entity.volunteer;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
@Table(
        name = "volunteer",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"email"})
        }
)
public class Volunteer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    @NotBlank(message = "Name is required")
    private String name;

    @Column(nullable = false, unique = true)
    @Email(message = "Please provide email with valid format")
    @NotBlank(message = "Email is required")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "Password is required")
    private String hashedPassword;

    @Column(nullable = false)
    @Min(value = 15, message = "Age must be at least 15")
    private int age;

    @Column(nullable = false)
    @NotBlank(message = "Address is required")
    private String address;

    @Column(nullable = false)
    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Please provide valid phone")
    private String phone;

    public String getEmail() {
        return email;
    }

    public String getHashedPassword() {
        return this.hashedPassword;
    }

    public void setHashedPassword(String plainPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.hashedPassword = passwordEncoder.encode(plainPassword);
    }

}