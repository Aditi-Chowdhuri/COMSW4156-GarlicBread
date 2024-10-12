package com.garlicbread.includify.entity.resource;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "resource_type", uniqueConstraints = { @UniqueConstraint(columnNames = {"title"}) })
public class ResourceType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Resource> resources;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @PreRemove
    private void preRemove() {
        if (resources != null && !resources.isEmpty()) {
            throw new IllegalStateException("Cannot delete resource type with existing resources.");
        }
    }
}

