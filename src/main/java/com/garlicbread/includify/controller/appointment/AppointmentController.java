package com.garlicbread.includify.controller.appointment;

import com.garlicbread.includify.service.appointment.AppointmentService;
import org.springframework.web.bind.annotation.RequestMapping;
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

  public AppointmentController(AppointmentService appointmentService) {
    this.appointmentService = appointmentService;
  }

  // add the required methods
}
