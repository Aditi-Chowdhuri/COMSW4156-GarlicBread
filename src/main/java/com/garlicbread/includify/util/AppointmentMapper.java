package com.garlicbread.includify.util;

import com.garlicbread.includify.entity.appointment.Appointment;
import com.garlicbread.includify.entity.organisation.Organisation;
import com.garlicbread.includify.entity.resource.Resource;
import com.garlicbread.includify.entity.user.User;
import com.garlicbread.includify.model.appointment.AppointmentRequest;

import java.util.List;

public class AppointmentMapper {
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

