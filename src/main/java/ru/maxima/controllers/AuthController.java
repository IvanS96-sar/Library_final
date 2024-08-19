package ru.maxima.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.maxima.dto.AuthDTO;
import ru.maxima.dto.PersonDTO;
import ru.maxima.models.Person;
import ru.maxima.service.PersonService;
import ru.maxima.util.JWTUtil;
import ru.maxima.validation.PersonValidator;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final PersonValidator personValidator;
    private final PersonService personService;
    private final JWTUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(PersonValidator personValidator, PersonService personService, JWTUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.personValidator = personValidator;
        this.personService = personService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;

    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody AuthDTO authDTO) {
        UsernamePasswordAuthenticationToken userToken =
                new UsernamePasswordAuthenticationToken(
                        authDTO.getName(), authDTO.getPassword()
                );
        try {
            authenticationManager.authenticate(userToken);
        } catch (Exception e) {
            return Map.of("error", "incorrect login or password");
        }

        String token = jwtUtil.generateToken(authDTO.getName());
        return Map.of("jwt-token", token);
    }


    @PostMapping("/registration")
    public Map<String, String> registration(@RequestBody @Valid PersonDTO personDTO,
                                            BindingResult bindingResult) {

        Person person = personService.convertFromDTOToPerson(personDTO);
        personValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors()) {
            return Map.of("message", "error body");
        }
        personService.save(person);
        String token = jwtUtil.generateToken(person.getName());
        return Map.of("jwt-token", token);
    }
}
