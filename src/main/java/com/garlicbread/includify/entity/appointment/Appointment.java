package com.garlicbread.includify.entity.appointment;

import com.garlicbread.includify.entity.organisation.Organisation;
import com.garlicbread.includify.entity.resource.Resource;
import com.garlicbread.includify.entity.user.User;
import com.garlicbread.includify.entity.volunteer.Volunteer;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "appointment")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "organisation_id", nullable = false)
    private Organisation organisation;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToMany(mappedBy = "appointment", fetch = FetchType.EAGER)
    private List<Resource> resources;

    @ManyToOne
    @JoinColumn(name = "volunteer_id")
    private Volunteer volunteer;

    @Column(nullable = false)
    private long timeStart; // milliseconds after midnight

    @Column(nullable = false)
    private long timeEnd; // milliseconds after midnight

    @Column
    private String date; // mmddyyyy
}
