package com.garlicbread.includify.repository.appointment;

import com.garlicbread.includify.entity.appointment.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing Appointment entities.
 * This interface extends JpaRepository to provide CRUD operations for Appointment objects.
 */
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, String> {

  // add required methods
}
