package com.garlicbread.includify.controller;

import jakarta.annotation.security.PermitAll;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * HomeController is responsible for handling the root ("/") HTTP requests.
 * It provides a simple welcome message and directs users
 * to the GitHub repository for more information.
 */
@Controller
public class HomeController {

  /**
   * Handles HTTP GET requests to the root URL ("/"). This method is accessible to all users.
   *
   * @return a ResponseEntity containing a welcome message and a link to the GitHub repository,
   *         along with an HTTP status of OK (200).
   */
  @RequestMapping("/")
  @PermitAll
  public ResponseEntity<String> index() {

    return new ResponseEntity<>("Welcome to the Includify Service !!! Checkout https://github"
            + ".com/Aditi-Chowdhuri/COMSW4156-GarlicBread/blob/main/README.md to get more details.",
        HttpStatus.OK);
  }
}
