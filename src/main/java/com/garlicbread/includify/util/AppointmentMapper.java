package com.garlicbread.includify.util;

import com.garlicbread.includify.entity.appointment.Appointment;
import com.garlicbread.includify.entity.organisation.Organisation;
import com.garlicbread.includify.entity.resource.Resource;
import com.garlicbread.includify.entity.user.User;
import com.garlicbread.includify.model.appointment.AppointmentRequest;
import java.util.List;

/**
 * Utility class for mapping an {@link AppointmentRequest} to an {@link Appointment}.
 */
public class AppointmentMapper {
  /**
   * Maps an {@link AppointmentRequest} to an {@link Appointment} entity.
   *
   * @param request      The appointment request containing user input details.
   * @param organisation The organisation associated with the appointment.
   * @param resources    The list of resources allocated for the appointment.
   * @param user         The user who booked the appointment.
   * @return A fully populated {@link Appointment} entity.
   */
  public static Appointment mapToAppointment(final AppointmentRequest request,
                                             final Organisation organisation,
                                             final List<Resource> resources,
                                             final User user) {

    Appointment appointment = new Appointment();
    appointment.setOrganisation(organisation);
    appointment.setUser(user);
    appointment.setResources(resources);
    appointment.setTimeStart(request.getTimeStart());
    appointment.setTimeEnd(request.getTimeEnd());
    appointment.setDate(request.getDate());
    return appointment;
  }
}

