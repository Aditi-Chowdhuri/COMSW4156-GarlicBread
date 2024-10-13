package com.garlicbread.includify.service.volunteer;

import com.garlicbread.includify.repository.volunteer.VolunteerRepository;
import org.springframework.stereotype.Service;

@Service
public class VolunteerService {

    private final VolunteerRepository volunteerRepository;

    public VolunteerService(VolunteerRepository volunteerRepository) {
        this.volunteerRepository = volunteerRepository;
    }

    // add the required methods

}
