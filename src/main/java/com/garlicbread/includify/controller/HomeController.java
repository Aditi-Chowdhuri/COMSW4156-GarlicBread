package com.garlicbread.includify.controller;

import jakarta.annotation.security.PermitAll;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

  @RequestMapping("/")
  @PermitAll
  public ResponseEntity<String> index() {
    return new ResponseEntity<>("Welcome to the Includify Service !!! Checkout https://github" +
        ".com/Aditi-Chowdhuri/COMSW4156-GarlicBread/blob/main/README.md to get more details.",
        HttpStatus.OK);
  }
}
