package com.garlicbread.includify.service.volunteer;

import com.garlicbread.includify.entity.volunteer.Volunteer;
import com.garlicbread.includify.repository.volunteer.VolunteerRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VolunteerService {

    private final VolunteerRepository volunteerRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public VolunteerService(VolunteerRepository volunteerRepository) {
        this.volunteerRepository = volunteerRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public List<Volunteer> getAllVolunteers() {
        return volunteerRepository.findAll();
    }

    public Optional<Volunteer> getVolunteerById(String id) {
        return volunteerRepository.findById(id);
    }

    public Volunteer addVolunteer(Volunteer volunteer) {
        volunteer.setHashedPassword(passwordEncoder.encode(volunteer.getHashedPassword()));
        return volunteerRepository.save(volunteer);
    }

    public void deleteVolunteer(String id) {
        volunteerRepository.deleteById(id);
    }
}

