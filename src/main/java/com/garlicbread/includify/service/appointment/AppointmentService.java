package com.garlicbread.includify.service.appointment;

import com.garlicbread.includify.repository.appointment.AppointmentRepository;
import org.springframework.stereotype.Service;

/**
 * Service class for managing appointments.
 * This class provides methods to handle appointment-related operations
 * and interacts with the AppointmentRepository for data persistence.
 */
@Service
public class AppointmentService {

  private final AppointmentRepository appointmentRepository;

  public AppointmentService(AppointmentRepository appointmentRepository) {
    this.appointmentRepository = appointmentRepository;
  }

  // add the required methods

}
