package ru.maxima.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.maxima.models.Person;

import java.util.Optional;

@Repository
public interface PeopleRepository extends JpaRepository<Person,  Long> {
    Optional<Person> findByName(String name);
}
