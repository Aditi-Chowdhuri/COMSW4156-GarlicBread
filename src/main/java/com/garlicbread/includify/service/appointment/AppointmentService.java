package com.garlicbread.includify.service.appointment;

import com.garlicbread.includify.entity.appointment.Appointment;
import com.garlicbread.includify.exception.ResourceNotFoundException;
import com.garlicbread.includify.repository.appointment.AppointmentRepository;
import java.util.List;
import java.util.Optional;
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

  /**
   * Creates a new appointment and saves it to the database.
   *
   * @param appointment the appointment to be created
   * @return the saved appointment entity
   */
  public Appointment createAppointment(Appointment appointment) {
    return appointmentRepository.save(appointment);
  }

  /**
   * Retrieves an appointment by its ID.
   * If the appointment is not found, a ResourceNotFoundException is thrown.
   *
   * @param id the ID of the appointment to retrieve
   * @return the appointment with the given ID
   * @throws ResourceNotFoundException if the appointment is not found
   */
  public Appointment getAppointmentById(String id) {
    Optional<Appointment> appointment = appointmentRepository.findById(id);
    return appointment.orElseThrow(
        () -> new ResourceNotFoundException("Appointment not found with id: " + id));
  }

  /**
   * Retrieves all appointments for a specific organisation by its ID.
   *
   * @param id the ID of the organisation
   * @return a list of appointments associated with the given organisation
   */
  public List<Appointment> getAllAppointmentsForOrganisation(String id) {
    return appointmentRepository.findByOrganisationId(id);
  }

  public int findAppointmentCountByDetails(String resourceId, String date, long startTime,
                                           long endTime) {
    return appointmentRepository.findAppointmentCountByDetails(resourceId, date, startTime,
        endTime);
  }

  /**
   * Retrieves all appointments for a specific user by their ID.
   *
   * @param id the ID of the user
   * @return a list of appointments associated with the given user
   */
  public List<Appointment> getAllAppointmentsForUser(String id) {
    return appointmentRepository.findByUserId(id);
  }

  /**
   * Deletes an appointment by its ID.
   * If the appointment does not exist, a ResourceNotFoundException is thrown.
   *
   * @param id the ID of the appointment to delete
   * @throws ResourceNotFoundException if the appointment is not found
   */
  public void deleteAppointment(String id) {
    if (appointmentRepository.existsById(id)) {
      appointmentRepository.deleteById(id);
    } else {
      throw new ResourceNotFoundException("Appointment not found with id: " + id);
    }
  }

}
