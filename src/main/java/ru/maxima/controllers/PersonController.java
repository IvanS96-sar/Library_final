package ru.maxima.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import ru.maxima.dto.PersonDTO;
import ru.maxima.exceptions.PersonErrorResponse;
import ru.maxima.exceptions.PersonNotFoundException;
import ru.maxima.models.Person;
import ru.maxima.service.BookService;
import ru.maxima.service.PersonService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/people")
public class PersonController {

    private final PersonService personService;


    @Autowired
    public PersonController(@Qualifier("personService") PersonService personService) {
        this.personService = personService;
    }

    @GetMapping()
    public List<PersonDTO> showAllPeople() {

        List<Person> allPeople = personService.getAllPeople();
        List<PersonDTO> personDTOList = new ArrayList<>();
        for (Person p : allPeople) {
            personDTOList.add(personService.convertToPersonDTO(p));
        }
        return personDTOList;

    }


    @GetMapping("/{id}")
    public PersonDTO showPersonById(@PathVariable("id") Long id) {

        Person byId = personService.findById(id);
        return personService.convertToPersonDTO(byId);
    }

    @ExceptionHandler({ PersonNotFoundException.class})
    public ResponseEntity<PersonErrorResponse> handleException(PersonNotFoundException ex) {
        PersonErrorResponse response = new PersonErrorResponse(ex.getMessage(), new Date());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @PostMapping("/create")
    public PersonDTO createNewPerson(@RequestBody PersonDTO personDTO) {

         return  personService.createPerson(personDTO);
    }


    @PutMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void editPerson(@PathVariable Long id, @RequestBody PersonDTO personDTO) {

        personService.update(id, personDTO);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deletePerson(@PathVariable("id") Integer idPerson) {

        personService.delete(idPerson);
    }

}