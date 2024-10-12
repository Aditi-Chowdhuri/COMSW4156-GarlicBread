package com.garlicbread.includify.entity.volunteer;

import jakarta.persistence.*;

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
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String hashedPassword;

    @Column(nullable = false)
    private int age;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String phone;
}

