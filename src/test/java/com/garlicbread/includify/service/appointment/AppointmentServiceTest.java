package com.garlicbread.includify.service.appointment;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.garlicbread.includify.entity.appointment.Appointment;
import com.garlicbread.includify.exception.ResourceNotFoundException;
import com.garlicbread.includify.repository.appointment.AppointmentRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class AppointmentServiceTest {

  @Mock
  private AppointmentRepository appointmentRepository;

  @InjectMocks
  private AppointmentService appointmentService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testCreateAppointment() {
    Appointment appointment = new Appointment();
    when(appointmentRepository.save(appointment)).thenReturn(appointment);
    Appointment savedAppointment = appointmentService.createAppointment(appointment);
    assertNotNull(savedAppointment);
    verify(appointmentRepository, times(1)).save(appointment);
  }

  @Test
  void testGetAppointmentById_Success() {
    String appointmentId = "123";
    Appointment appointment = new Appointment();
    when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));
    Appointment result = appointmentService.getAppointmentById(appointmentId);
    assertNotNull(result);
    verify(appointmentRepository, times(1)).findById(appointmentId);
  }

  @Test
  void testGetAppointmentById_NotFound() {
    String appointmentId = "123";
    when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.empty());
    ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
        () -> appointmentService.getAppointmentById(appointmentId));
    assertEquals("Appointment not found with id: 123", exception.getMessage());
    verify(appointmentRepository, times(1)).findById(appointmentId);
  }

  @Test
  void testGetAllAppointmentsForOrganisation() {
    String organisationId = "org123";
    List<Appointment> appointments = Arrays.asList(new Appointment(), new Appointment());
    when(appointmentRepository.findByOrganisationId(organisationId)).thenReturn(appointments);
    List<Appointment> result = appointmentService.getAllAppointmentsForOrganisation(organisationId);
    assertEquals(2, result.size());
    verify(appointmentRepository, times(1)).findByOrganisationId(organisationId);
  }

  @Test
  void testGetAllAppointmentsForUser() {
    String userId = "user123";
    List<Appointment> appointments = Arrays.asList(new Appointment(), new Appointment());
    when(appointmentRepository.findByUserId(userId)).thenReturn(appointments);
    List<Appointment> result = appointmentService.getAllAppointmentsForUser(userId);
    assertEquals(2, result.size());
    verify(appointmentRepository, times(1)).findByUserId(userId);
  }

  @Test
  void testDeleteAppointment_Success() {
    String appointmentId = "123";
    when(appointmentRepository.existsById(appointmentId)).thenReturn(true);
    appointmentService.deleteAppointment(appointmentId);
    verify(appointmentRepository, times(1)).existsById(appointmentId);
    verify(appointmentRepository, times(1)).deleteById(appointmentId);
  }

  @Test
  void testDeleteAppointment_NotFound() {
    String appointmentId = "123";
    when(appointmentRepository.existsById(appointmentId)).thenReturn(false);
    ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
        () -> appointmentService.deleteAppointment(appointmentId));
    assertEquals("Appointment not found with id: 123", exception.getMessage());
    verify(appointmentRepository, times(1)).existsById(appointmentId);
    verify(appointmentRepository, times(0)).deleteById(appointmentId);
  }
}

