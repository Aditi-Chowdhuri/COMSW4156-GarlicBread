package com.garlicbread.includify.controller.volunteer;

import com.garlicbread.includify.entity.resource.Resource;
import com.garlicbread.includify.entity.volunteer.Volunteer;
import com.garlicbread.includify.service.volunteer.VolunteerService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/volunteer")
public class VolunteerController {

    private final VolunteerService volunteerService;

    public VolunteerController(VolunteerService volunteerService) {
        this.volunteerService = volunteerService;
    }

    @GetMapping("/all")
    public List<Volunteer> getAllVolunteers() {
        return volunteerService.getAllVolunteers();
    }

    @GetMapping("/{id}")
    public Optional<Volunteer> getVolunteerById(@PathVariable String id) {
        return volunteerService.getVolunteerById(id);
    }

    @PostMapping("/add")
    public Volunteer addVolunteer(@RequestBody Volunteer volunteer) {
        return volunteerService.addVolunteer(volunteer);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteResource(@PathVariable String id) {
        volunteerService.deleteVolunteer(id);
    }

    // add the required methods
}
