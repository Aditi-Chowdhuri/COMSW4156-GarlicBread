package com.garlicbread.includify.entity.resource.types;

import com.garlicbread.includify.entity.resource.Resource;
import jakarta.persistence.*;

@Entity
@Table(name = "resource_tool")
public class ResourceTool {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    // one resource_tool can be mapped to only one resource
    @OneToOne(cascade = CascadeType.REMOVE)
    @MapsId
    @JoinColumn(name = "id")
    private Resource resource;

    @Column(nullable = false)
    private int count;
}

