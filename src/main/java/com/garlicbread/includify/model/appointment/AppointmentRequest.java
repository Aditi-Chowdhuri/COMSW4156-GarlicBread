package com.garlicbread.includify.model.appointment;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public class AppointmentRequest {

  @NotBlank(message = "Organisation id is required")
  private String organisationId;

  @NotBlank(message = "User id is required")
  private String userId;

  private List<String> resourceIds;

  @Min(value = 1, message = "timeStart should at least be 1 millisecond")
  private long timeStart; // milliseconds after midnight

  @Min(value = 1, message = "timeEnd should at least be 1 millisecond")
  private long timeEnd; // milliseconds after midnight

  @Pattern(regexp = "^(0[1-9]|1[0-2])([0-2][1-9]|[3][0-1])(19|20)\\d\\d$", message = "Invalid date"
      + " format. It should be mmddyyyy.")
  private String date; // mmddyyyy

  public String getOrganisationId() {
    return organisationId;
  }

  public void setOrganisationId(String organisationId) {
    this.organisationId = organisationId;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public List<String> getResourceIds() {
    return resourceIds;
  }

  public void setResourceIds(List<String> resourceIds) {
    this.resourceIds = resourceIds;
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
