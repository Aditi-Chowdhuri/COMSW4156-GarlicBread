package com.garlicbread.includify.entity.appointment;

import com.garlicbread.includify.entity.organisation.Organisation;
import com.garlicbread.includify.entity.resource.Resource;
import com.garlicbread.includify.entity.user.User;
import com.garlicbread.includify.entity.volunteer.Volunteer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.List;

/**
 * Represents an appointment entity in the system.
 * An appointment is associated with an organization, a user,
 * a list of resources, and optionally a volunteer.
 */
@Entity
@Table(name = "appointment")
public class Appointment {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @ManyToOne
  @JoinColumn(name = "organisation_id", nullable = false)
  private Organisation organisation;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "appointment_resources", joinColumns = @JoinColumn(name = "appointment_id"),
      inverseJoinColumns = @JoinColumn(name = "resource_id"))
  private List<Resource> resources;

  @ManyToOne
  @JoinColumn(name = "volunteer_id")
  private Volunteer volunteer;

  @Column(nullable = false)
  private long timeStart; // milliseconds after midnight

  @Column(nullable = false)
  private long timeEnd; // milliseconds after midnight

  @Column(nullable = false)
  private String date; // mmddyyyy

  public String getId() {
    return id;
  }

  public Organisation getOrganisation() {
    return organisation;
  }

  public void setOrganisation(Organisation organisation) {
    this.organisation = organisation;
  }

  public User getUser() {
    return user;
  }


  public void setUser(User user) {
    this.user = user;
  }

  public List<Resource> getResources() {
    return resources;
  }

  public Volunteer getVolunteer() {
    return volunteer;
  }

  public void setVolunteer(Volunteer volunteer) {
    this.volunteer = volunteer;
  }

  public void setResources(List<Resource> resources) {
    this.resources = resources;
  }

  public long getTimeStart() {
    return timeStart;
  }

  public void setTimeStart(long timeStart) {
    this.timeStart = timeStart;
  }

  public long getTimeEnd() {
    return timeEnd;
  }

  public void setTimeEnd(long timeEnd) {
    this.timeEnd = timeEnd;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }
}
