package com.garlicbread.includify.controller.volunteer;

import com.garlicbread.includify.entity.volunteer.Volunteer;
import com.garlicbread.includify.exception.ResourceNotFoundException;
import com.garlicbread.includify.service.volunteer.VolunteerService;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public ResponseEntity<List<Volunteer>> getAllVolunteers() {
        List<Volunteer> volunteers = volunteerService.getAllVolunteers();
        if (volunteers.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(volunteers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Volunteer> getVolunteerById(@PathVariable String id) {
        Optional<Volunteer> volunteer = volunteerService.getVolunteerById(id);
        return volunteer
            .map(ResponseEntity::ok)
            .orElseThrow(() -> new ResourceNotFoundException("Volunteer not found with id: " + id));
    }

    @PostMapping("/add")
    @PermitAll
    public ResponseEntity<Volunteer> addVolunteer(@Valid @RequestBody Volunteer volunteer) {
        Volunteer newVolunteer =  volunteerService.addVolunteer(volunteer);
        return new ResponseEntity<>(newVolunteer, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('VOLUNTEER')")
    public ResponseEntity<String> deleteResource(@PathVariable String id) {
        Optional<Volunteer> volunteer = volunteerService.getVolunteerById(id);
        if (volunteer.isPresent()) {
            volunteerService.deleteVolunteer(id);
            return new ResponseEntity<>("Volunteer deleted successfully", HttpStatus.NO_CONTENT);
        } else {
            throw new ResourceNotFoundException("Volunteer not found with id: " + id);
        }
    }
}
