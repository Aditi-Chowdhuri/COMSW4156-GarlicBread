package com.garlicbread.includify.repository.appointment;

import com.garlicbread.includify.entity.appointment.Appointment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing Appointment entities.
 * This interface extends JpaRepository to provide CRUD operations for Appointment objects.
 */
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, String> {

  List<Appointment> findByOrganisationId(String organisationId);

  List<Appointment> findByUserId(String userId);

  @Query(value = "select count(*) from appointment_resources inner join appointment on "
      + "appointment.id = appointment_resources.appointment_id where resource_id = :resourceId and "
      + "`date` = :date and (time_end >= :timeStart and time_start <= :timeEnd)",
      nativeQuery = true)
  int findAppointmentCountByDetails(@Param("resourceId") String resourceId,
                                    @Param("date") String date, @Param("timeStart") long timeStart,
                                    @Param("timeEnd") long timeEnd);

}
