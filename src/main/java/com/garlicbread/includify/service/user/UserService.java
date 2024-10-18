package com.garlicbread.includify.service.user;

import com.garlicbread.includify.entity.user.User;
import com.garlicbread.includify.exception.ResourceNotFoundException;
import com.garlicbread.includify.repository.user.UserRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  public Optional<User> getUserById(String id) {
    return userRepository.findById(id);
  }

  public User createUser(User user) {
    return userRepository.save(user);
  }

  public User updateUser(String id, User userDetails) {
    return userRepository.findById(id).map(user -> {
      user.setName(userDetails.getName());
      user.setAge(userDetails.getAge());
      user.setEmail(userDetails.getEmail());
      user.setPasswordWithoutHash(userDetails.getPassword());
      return userRepository.save(user);
    }).orElseThrow(() -> new ResourceNotFoundException("User not " + "found with id " + id));
  }

  public void deleteUser(String id) {
    userRepository.deleteById(id);
  }

  // add the required methods

}