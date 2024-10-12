package com.garlicbread.includify.entity.resource.types;

import com.garlicbread.includify.entity.resource.Resource;
import jakarta.persistence.*;

@Entity
@Table(name = "resource_contact")
public class ResourceContact {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    // one resource_contact can be mapped to only one resource
    @OneToOne(cascade = CascadeType.REMOVE)
    @MapsId
    @JoinColumn(name = "id")
    private Resource resource;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String latitude;

    @Column(nullable = false)
    private String longitude;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private double distance; // distance in miles
}

