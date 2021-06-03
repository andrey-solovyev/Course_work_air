package org.example.data.repository;

import org.example.data.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface PersonRepository extends JpaRepository<Person, UUID> {
    Person findByEmail(String email);

}
