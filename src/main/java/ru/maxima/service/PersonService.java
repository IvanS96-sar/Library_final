package ru.maxima.service;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.maxima.dto.PersonDTO;
import ru.maxima.exceptions.PersonNotFoundException;
import ru.maxima.models.Book;
import ru.maxima.models.Person;
import ru.maxima.repositories.PeopleRepository;
import ru.maxima.util.JWTUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
public class PersonService {
    private final PeopleRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final JWTUtil jwtUtil;

    public PersonService(PeopleRepository repository, PasswordEncoder passwordEncoder, ModelMapper modelMapper, JWTUtil jwtUtil) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.jwtUtil = jwtUtil;
    }
    @Transactional
    public List<Person> getAllPeople() {
        
        return repository.findAll();
    }

    @Transactional
    public Person findById(Long id) {
        Optional<Person> byId = repository.findById(id);
        return byId.orElseThrow(() -> new PersonNotFoundException("Person is not found"));

    }

    @Transactional
    public void save(Person person) {
        enrich(person);
        repository.save(person);
    }
    @Transactional
    public PersonDTO createPerson(PersonDTO personDTO) {
        Person person = convertFromDTOToPerson(personDTO);
        //person.setCreatedPerson(creator);
        person.setCreatedAt(LocalDateTime.now());
        repository.save(person);
        return convertToPersonDTO(person);
    }


    @Transactional
    public void update(Long id,PersonDTO personDTO) {
        Person person = convertFromDTOToPerson(personDTO);
        person.setIdPerson(id);
        repository.save(person);

    }

    @Transactional
    public void delete(Integer id) {
        Person person =findById(Long.valueOf(id));
        person.setRemoved(true);
        //person.setRemovedPerson(deletedBy);
        person.setRemovedAt(LocalDateTime.now());
        repository.save(person);

    }


    @Transactional
    public List<Book> bookByPerson(Long idPerson){

        return repository.findById(idPerson).orElse(null).getBooks();
    }

    public PersonDTO convertToPersonDTO(Person person) {
        return modelMapper.map(person, PersonDTO.class);

    }


    public Person convertFromDTOToPerson(PersonDTO personDTO) {
        Person person = modelMapper.map(personDTO, Person.class);

        enrich(person);
        return person;
    }

    private void enrich(Person p) {
        p.setCreatedAt(LocalDateTime.now());
        p.setCreatedPerson ("ROLE_ADMIN");
        p.setPassword(passwordEncoder.encode(p.getPassword()));
        p.setRole("ROLE_USER");
        p.setRemoved(false);
    }

}
