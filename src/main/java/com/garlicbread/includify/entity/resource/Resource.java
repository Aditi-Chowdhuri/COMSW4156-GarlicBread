package com.garlicbread.includify.entity.resource;

import com.garlicbread.includify.entity.appointment.Appointment;
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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "resource_types",
        joinColumns = @JoinColumn(name = "resource_id"),
        inverseJoinColumns = @JoinColumn(name = "type_id")
    )
    private List<ResourceType> resourceType;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "resource_user_categories",
        joinColumns = @JoinColumn(name = "resource_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<UserCategory> targetUserCategory;

    @Column(nullable = false)
    private String title;

    private String description;

    private String usageInstructions;

    @ManyToMany(mappedBy = "resources")
    private List<Appointment> appointments;
}

