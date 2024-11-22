package com.garlicbread.includify.entity.resource.types;

import com.garlicbread.includify.entity.resource.Resource;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

/**
 * Entity representing a service provided by a resource.
 * This class maps to the 'resource_service' table in the database.
 * Each ResourceService is associated with a single {@link Resource}.
 */
@Entity
@Table(name = "resource_service")
public class ResourceService {

  @Id
  private String id;

  // one resource_service can be mapped to only one resource
  @OneToOne
  @MapsId
  @JoinColumn(name = "id")
  private Resource resource;

  @Column(nullable = false)
  @Min(value = 1, message = "Service start time should be at least 1")
  private long timeStart; // milliseconds after midnight

  @Column(nullable = false)
  @Min(value = 1, message = "Service end time should be at least 1")
  private long timeEnd; // milliseconds after midnight

  @Column
  @Pattern(regexp = "^[01]{7}$", message = "Days should be a 7-character binary string (e.g., "
      + "0111110).")
  private String days; // a seven digit binary string starting from Sunday
  // (1 denotes the service is available that day and vice versa)

  @Column
  @Pattern(regexp = "^(0[1-9]|1[0-2])([0-2][1-9]|[3][0-1])(19|20)\\d\\d$", message = "Invalid date"
      + " format. It should be mmddyyyy.")
  private String date; // mmddyyyy

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Resource getResource() {
    return resource;
  }

  public void setResource(Resource resource) {
    this.resource = resource;
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

  public String getDays() {
    return days;
  }

  public void setDays(String days) {
    this.days = days;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }
}

