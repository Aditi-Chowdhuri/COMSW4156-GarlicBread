package com.garlicbread.includify.entity.resource;

import com.garlicbread.includify.entity.organisation.Organisation;
import com.garlicbread.includify.entity.user.UserCategory;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(
    name = "resource",
    uniqueConstraints = {
        // An organisation can have at most one resource with a given title
        @UniqueConstraint(columnNames = {"organisation_id", "title"})
    }
)
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    // many resources can belong to one organisation
    @ManyToOne
    @JoinColumn(name = "organisation_id", nullable = false)
    private Organisation organisation;

    @ManyToMany(mappedBy = "resource", fetch = FetchType.EAGER)
    private List<ResourceType> resourceType;

    @ManyToMany(mappedBy = "resource", fetch = FetchType.EAGER)
    private List<UserCategory> targetUserCategory;

    @Column(nullable = false)
    private String title;

    private String description;

    private String usageInstructions;
}

