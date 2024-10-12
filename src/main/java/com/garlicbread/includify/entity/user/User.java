package com.garlicbread.includify.entity.user;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "user", uniqueConstraints = { @UniqueConstraint(columnNames = {"email"}) })
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int age;

    @ManyToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<UserCategory> categories;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String hashedPassword;
}
