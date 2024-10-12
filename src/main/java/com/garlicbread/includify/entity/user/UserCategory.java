package com.garlicbread.includify.entity.user;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "user_category", uniqueConstraints = { @UniqueConstraint(columnNames = {"title"}) })
public class UserCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<User> users;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @PreRemove
    private void preRemove() {
        if (users != null && !users.isEmpty()) {
            throw new IllegalStateException("Cannot delete user category with existing users.");
        }
    }
}

