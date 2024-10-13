package com.garlicbread.includify.entity.user;

import com.garlicbread.includify.entity.resource.Resource;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "user_category", uniqueConstraints = { @UniqueConstraint(columnNames = {"title"}) })
public class UserCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY)
    private List<User> users;

    @ManyToMany(mappedBy = "targetUserCategory", fetch = FetchType.LAZY)
    private List<Resource> resources;

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

