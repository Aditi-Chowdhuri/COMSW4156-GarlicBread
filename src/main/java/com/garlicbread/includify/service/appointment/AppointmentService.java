package com.garlicbread.includify.service.appointment;

import com.garlicbread.includify.entity.appointment.Appointment;
import com.garlicbread.includify.exception.ResourceNotFoundException;
import com.garlicbread.includify.repository.appointment.AppointmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

  public Appointment createAppointment(Appointment appointment) {
    return appointmentRepository.save(appointment);
  }

  public Appointment getAppointmentById(String id) {
    Optional<Appointment> appointment = appointmentRepository.findById(id);
    return appointment.orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + id));
  }

  public List<Appointment> getAllAppointmentsForOrganisation(String id) {
    return appointmentRepository.findByOrganisationId(id);
  }

  public List<Appointment> getAllAppointmentsForUser(String id) {
    return appointmentRepository.findByUserId(id);
  }

  public void deleteAppointment(String id) {
    if (appointmentRepository.existsById(id)) {
      appointmentRepository.deleteById(id);
    } else {
      throw new ResourceNotFoundException("Appointment not found with id: " + id);
    }
  }

}
