package com.garlicbread.includify.controller.volunteer;

import com.garlicbread.includify.entity.volunteer.Volunteer;
import com.garlicbread.includify.exception.ResourceNotFoundException;
import com.garlicbread.includify.profile.volunteer.VolunteerDetails;
import com.garlicbread.includify.service.volunteer.VolunteerService;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for managing volunteer operations.
 * Provides endpoints for adding, retrieving, and deleting volunteers.
 */
@RestController
@RequestMapping("/volunteer")
public class VolunteerController {

  private final VolunteerService volunteerService;

  /**
   * Constructs a VolunteerController with the specified VolunteerService.
   *
   * @param volunteerService the service used for volunteer operations
   */
  public VolunteerController(VolunteerService volunteerService) {
    this.volunteerService = volunteerService;
  }

  /**
   * Retrieves all volunteers.
   *
   * @return ResponseEntity containing a list of volunteers and HTTP status
   */
  @GetMapping("/all")
  public ResponseEntity<List<Volunteer>> getAllVolunteers() {
    List<Volunteer> volunteers = volunteerService.getAllVolunteers();
    if (volunteers.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    return new ResponseEntity<>(volunteers, HttpStatus.OK);
  }

  /**
   * Retrieves a volunteer by their ID.
   *
   * @param id the ID of the volunteer to retrieve
   * @return ResponseEntity containing the volunteer and HTTP status
   * @throws ResourceNotFoundException if no volunteer is found with the given ID
   */
  @GetMapping("/{id}")
  public ResponseEntity<Volunteer> getVolunteerById(@PathVariable String id) {
    Optional<Volunteer> volunteer = volunteerService.getVolunteerById(id);
    return volunteer.map(ResponseEntity::ok).orElseThrow(
        () -> new ResourceNotFoundException("Volunteer " + "not found with id: " + id));
  }

  /**
   * Adds a new volunteer.
   *
   * @param volunteer the volunteer to add
   * @return ResponseEntity containing the newly created volunteer and HTTP status
   */
  @PostMapping("/add")
  @PermitAll
  public ResponseEntity<Volunteer> addVolunteer(@Valid @RequestBody Volunteer volunteer) {
    Volunteer newVolunteer = volunteerService.addVolunteer(volunteer);
    return new ResponseEntity<>(newVolunteer, HttpStatus.CREATED);
  }

  /**
   * Deletes a volunteer by their ID.
   *
   * @param id the ID of the volunteer to delete
   * @return ResponseEntity containing a success message and HTTP status
   * @throws ResourceNotFoundException if no volunteer is found with the given ID
   */
  @DeleteMapping("/delete/{id}")
  @PreAuthorize("hasAnyAuthority('VOLUNTEER')")
  public ResponseEntity<String> deleteResource(@PathVariable String id,
                                               Authentication authentication) {
    String authenticatedVolunteerId = ((VolunteerDetails) authentication.getPrincipal()).getId();
    if (!authenticatedVolunteerId.equals(id)) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    Optional<Volunteer> volunteer = volunteerService.getVolunteerById(id);
    if (volunteer.isPresent()) {
      volunteerService.deleteVolunteer(id);
      return new ResponseEntity<>("Volunteer deleted successfully", HttpStatus.NO_CONTENT);
    } else {
      throw new ResourceNotFoundException("Volunteer not found with id:" + " " + id);
    }
  }
}
