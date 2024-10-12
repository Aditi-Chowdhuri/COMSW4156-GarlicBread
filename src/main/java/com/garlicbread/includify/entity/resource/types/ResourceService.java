package com.garlicbread.includify.entity.resource.types;

import com.garlicbread.includify.entity.resource.Resource;
import jakarta.persistence.*;

@Entity
@Table(name = "resource_service")
public class ResourceService {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    // one resource_service can be mapped to only one resource
    @OneToOne(cascade = CascadeType.REMOVE)
    @MapsId
    @JoinColumn(name = "id")
    private Resource resource;

    @Column(nullable = false)
    private long timeStart; // milliseconds after midnight

    @Column(nullable = false)
    private long timeEnd; // milliseconds after midnight

    @Column
    private String days; // a seven digit binary string starting from Sunday (1 denotes the service is available that day and vice versa)

    @Column
    private String date; // mmddyyyy
}

