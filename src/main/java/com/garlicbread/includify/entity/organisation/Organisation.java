package com.garlicbread.includify.entity.organisation;

import com.garlicbread.includify.entity.resource.Resource;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "organisation", uniqueConstraints = { @UniqueConstraint(columnNames = {"email"}) })
public class Organisation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String hashedPassword;

    private String description;

    @Column(nullable = false)
    private String latitude;

    @Column(nullable = false)
    private String longitude;

    @Column(nullable = false)
    private String address;

    // one organisation can have many resources
    @OneToMany(mappedBy = "organisation", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Resource> resources;
}
