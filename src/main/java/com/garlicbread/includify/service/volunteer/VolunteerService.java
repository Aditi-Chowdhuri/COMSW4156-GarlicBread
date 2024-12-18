package com.garlicbread.includify.service.volunteer;

import com.garlicbread.includify.entity.volunteer.Volunteer;
import com.garlicbread.includify.repository.volunteer.VolunteerRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * Service class for managing volunteer operations.
 * This class provides methods to perform CRUD operations on volunteers.
 */
@Service
public class VolunteerService {

  private final VolunteerRepository volunteerRepository;

  public VolunteerService(VolunteerRepository volunteerRepository) {
    this.volunteerRepository = volunteerRepository;
  }

  public List<Volunteer> getAllVolunteers() {
    return volunteerRepository.findAll();
  }

  public Optional<Volunteer> getVolunteerById(String id) {
    return volunteerRepository.findById(id);
  }

  public Volunteer addVolunteer(Volunteer volunteer) {
    return volunteerRepository.save(volunteer);
  }

  public void deleteVolunteer(String id) {
    volunteerRepository.deleteById(id);
  }
}

