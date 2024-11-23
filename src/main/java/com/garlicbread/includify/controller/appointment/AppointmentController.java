package com.garlicbread.includify.controller.appointment;

import static com.garlicbread.includify.util.AppointmentMapper.mapToAppointment;

import com.garlicbread.includify.entity.appointment.Appointment;
import com.garlicbread.includify.entity.organisation.Organisation;
import com.garlicbread.includify.entity.resource.Resource;
import com.garlicbread.includify.entity.resource.ResourceType;
import com.garlicbread.includify.entity.resource.types.ResourceTool;
import com.garlicbread.includify.entity.user.User;
import com.garlicbread.includify.model.appointment.AppointmentRequest;
import com.garlicbread.includify.profile.organisation.OrganisationDetails;
import com.garlicbread.includify.profile.user.UserDetails;
import com.garlicbread.includify.profile.volunteer.VolunteerDetails;
import com.garlicbread.includify.service.appointment.AppointmentService;
import com.garlicbread.includify.service.organisation.OrganisationService;
import com.garlicbread.includify.service.resource.ResourceService;
import com.garlicbread.includify.service.resource.types.ResourceToolService;
import com.garlicbread.includify.service.user.UserService;
import com.garlicbread.includify.service.volunteer.VolunteerService;
import com.garlicbread.includify.util.Constants;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling appointment-related requests.
 * This class is responsible for managing the appointment endpoints
 * and interacting with the AppointmentService to perform operations related to appointments.
 */
@RestController
@RequestMapping("/appointment")
public class AppointmentController {

  private final AppointmentService appointmentService;
  private final ResourceService resourceService;
  private final OrganisationService organisationService;
  private final UserService userService;
  private final VolunteerService volunteerService;
  private final ResourceToolService resourceToolService;

  /**
   * Constructor for AppointmentController.
   *
   * @param appointmentService Service for managing appointments.
   * @param resourceService    Service for managing resources.
   * @param organisationService Service for managing organisations.
   * @param userService         Service for managing users.
   * @param volunteerService    Service for managing volunteers.
   * @param resourceToolService Service for managing resource tools.
   */
  public AppointmentController(AppointmentService appointmentService,
                               ResourceService resourceService,
                               OrganisationService organisationService,
                               UserService userService,
                               VolunteerService volunteerService,
                               ResourceToolService resourceToolService) {
    this.appointmentService = appointmentService;
    this.resourceService = resourceService;
    this.organisationService = organisationService;
    this.userService = userService;
    this.volunteerService = volunteerService;
    this.resourceToolService = resourceToolService;
  }

  /**
   * Creates a new appointment.
   *
   * @param appointmentRequest The request payload containing appointment details.
   * @param authentication     The authentication object to verify user permissions.
   * @return A ResponseEntity containing the created appointment or an error status.
   */
  @PostMapping
  @PreAuthorize("hasAnyAuthority('ORGANISATION', 'USER')")
  public ResponseEntity<Object> createAppointment(@RequestBody
                                                    AppointmentRequest appointmentRequest,
                                                  Authentication authentication) {
    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
    for (GrantedAuthority authority : authorities) {
      if (authority.getAuthority().equals(Constants.AUTHORITY_ORGANISATION)) {
        String authenticatedOrganisationId = ((OrganisationDetails) authentication.getPrincipal())
                .getId();
        if (!authenticatedOrganisationId.equals(appointmentRequest.getOrganisationId())) {
          return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
      } else if (authority.getAuthority().equals(Constants.AUTHORITY_USER)) {
        String authenticatedUserId = ((UserDetails) authentication.getPrincipal()).getId();
        if (!authenticatedUserId.equals(appointmentRequest.getUserId())) {
          return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
      }
    }

    List<Resource> resources = new ArrayList<>();
    List<String> resourceIds = appointmentRequest.getResourceIds();
    if (resourceIds != null) {
      for (String resourceId : resourceIds) {
        Optional<Resource> resource = resourceService.getResourceById(resourceId);
        if (resource.isEmpty()) {
          return new ResponseEntity<>("Resource does not exist", HttpStatus.BAD_REQUEST);
        }
        if (!Objects.equals(resource.get().getOrganisation(),
            appointmentRequest.getOrganisationId())) {
          return new ResponseEntity<>("Invalid Resource", HttpStatus.BAD_REQUEST);
        }
        resources.add(resource.get());

        List<ResourceType> resourceTypes = resource.get().getResourceType();
        for (ResourceType resourceType : resourceTypes) {
          if (resourceType.getId() == 4) {
            Optional<ResourceTool> resourceTool = resourceToolService.getResourceToolById(
                resourceId);
            if (resourceTool.isPresent() && resourceTool.get().getCount() <= 0) {
              return new ResponseEntity<>("Resource not available: " + resourceId,
                  HttpStatus.BAD_REQUEST);
            }
          }
        }
      }
    }

    Optional<Organisation> organisation = organisationService.getOrganisationById(appointmentRequest
        .getOrganisationId());
    if (organisation.isEmpty()) {
      return new ResponseEntity<>("Organisation does not exist", HttpStatus.BAD_REQUEST);
    }

    Optional<User> user = userService.getUserById(appointmentRequest.getUserId());
    if (user.isEmpty()) {
      return new ResponseEntity<>("User does not exist", HttpStatus.BAD_REQUEST);
    }

    if (appointmentRequest.getTimeStart() > appointmentRequest.getTimeEnd()) {
      return new ResponseEntity<>("Invalid time range: timeStart must be before timeEnd",
          HttpStatus.BAD_REQUEST);
    }

    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMddyyyy");
    try {
      LocalDate appointmentDate = LocalDate.parse(appointmentRequest.getDate(), dateFormatter);
      if (!appointmentDate.isAfter(LocalDate.now())) {
        return new ResponseEntity<>("Invalid date: date must be in the future",
            HttpStatus.BAD_REQUEST);
      }
    } catch (DateTimeParseException e) {
      return new ResponseEntity<>("Invalid date format: expected mmddyyyy", HttpStatus.BAD_REQUEST);
    }

    for (Resource resource : resources) {
      List<ResourceType> resourceTypes = resource.getResourceType();
      for (ResourceType resourceType : resourceTypes) {
        if (resourceType.getId() == 4) {
          Optional<ResourceTool> resourceTool = resourceToolService
              .getResourceToolById(resource.getId());
          if (resourceTool.isPresent()) {
            resourceTool.get().setCount(resourceTool.get().getCount() - 1);
            resourceToolService.addResourceTool(resourceTool.get());
          }
        }
      }
    }

    Appointment createdAppointment = appointmentService.createAppointment(mapToAppointment(
            appointmentRequest,
            organisation.get(),
            resources,
            user.get()
    ));
    return ResponseEntity.status(HttpStatus.OK).body(createdAppointment);
  }

  /**
   * Retrieves all appointments for a specific organisation.
   *
   * @param organisationId  The ID of the organisation.
   * @param authentication  The authentication object to verify user permissions.
   * @return A ResponseEntity containing a list of appointments for the organisation.
   */
  @GetMapping("/organisation")
  @PreAuthorize("hasAnyAuthority('ORGANISATION', 'VOLUNTEER')")
  public ResponseEntity<List<Appointment>> getAllAppointmentsForOrganisation(
      @RequestParam(name = "organisation") String organisationId, Authentication authentication) {
    Collection<? extends GrantedAuthority> authorities = authentication
        .getAuthorities();
    for (GrantedAuthority authority : authorities) {
      if (authority.getAuthority().equals(Constants.AUTHORITY_ORGANISATION)) {
        String authenticatedOrganisationId = ((OrganisationDetails) authentication.getPrincipal())
                .getId();
        if (!authenticatedOrganisationId.equals(organisationId)) {
          return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
      }
    }

    List<Appointment> appointments = appointmentService.getAllAppointmentsForOrganisation(
        organisationId);
    return ResponseEntity.ok(appointments);
  }

  /**
   * Retrieves all appointments for the currently authenticated user.
   *
   * @param authentication The authentication object to retrieve the user ID.
   * @return A ResponseEntity containing a list of appointments for the user.
   */
  @GetMapping("/user")
  @PreAuthorize("hasAuthority('USER')")
  public ResponseEntity<List<Appointment>> getAllAppointmentsForUser(
      Authentication authentication) {
    String authenticatedUserId = ((UserDetails) authentication.getPrincipal()).getId();
    List<Appointment> appointments = appointmentService.getAllAppointmentsForUser(
        authenticatedUserId);
    return ResponseEntity.ok(appointments);
  }

  /**
   * Deletes an appointment by its ID.
   *
   * @param id              The ID of the appointment to delete.
   * @param authentication  The authentication object to verify user permissions.
   * @return A ResponseEntity indicating the result of the operation.
   */
  @DeleteMapping("/{id}")
  @PreAuthorize("hasAnyAuthority('ORGANISATION', 'USER')")
  public ResponseEntity<Void> deleteAppointment(@PathVariable String id,
                                                Authentication authentication) {
    Appointment appointment = appointmentService.getAppointmentById(id);

    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
    for (GrantedAuthority authority : authorities) {
      if (authority.getAuthority().equals(Constants.AUTHORITY_ORGANISATION)) {
        String authenticatedOrganisationId = ((OrganisationDetails) authentication.getPrincipal())
                .getId();
        if (!authenticatedOrganisationId.equals(appointment.getOrganisation().getId())) {
          return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
      } else if (authority.getAuthority().equals(Constants.AUTHORITY_USER)) {
        String authenticatedUserId = ((UserDetails) authentication.getPrincipal()).getId();
        if (!authenticatedUserId.equals(appointment.getUser().getId())) {
          return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
      }
    }
    appointmentService.deleteAppointment(id);
    return ResponseEntity.noContent().build();
  }

  /**
   * Assigns a volunteer to an appointment.
   *
   * @param id              The ID of the appointment to update.
   * @param authentication  The authentication object to retrieve the volunteer ID.
   * @return A ResponseEntity containing the updated appointment or an error status.
   */
  @PostMapping("/{id}/volunteer")
  @PreAuthorize("hasAuthority('VOLUNTEER')")
  public ResponseEntity<Object> addVolunteerToAppointment(@PathVariable String id,
                                                          Authentication authentication) {

    Appointment appointment = appointmentService.getAppointmentById(id);

    String volunteerId = ((VolunteerDetails) authentication.getPrincipal()).getId();

    if (appointment.getVolunteer() != null) {
      return new ResponseEntity<>("Appointment already has a volunteer assigned",
          HttpStatus.BAD_REQUEST);
    }

    appointment.setVolunteer(volunteerService.getVolunteerById(volunteerId).get());
    appointment = appointmentService.createAppointment(appointment);

    return ResponseEntity.status(HttpStatus.OK).body(appointment);
  }
}
