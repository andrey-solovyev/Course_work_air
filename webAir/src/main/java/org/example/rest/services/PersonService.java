package org.example.rest.services;

import org.example.data.dto.RegistrationDto;
import org.example.data.entity.Person;
import org.example.data.entity.Role;
import org.example.data.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class PersonService implements UserDetailsService {
    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Person person=personRepository.findByEmail(username);
        return person;
    }
    public boolean addUser(RegistrationDto registrationDto) {
        Person userFromDb = personRepository.findByEmail(registrationDto.getEmail());

        if (userFromDb != null) {
            return false;
        }
        Person person = new Person();
        person.setEmail(registrationDto.getEmail());
        person.setFirstName(registrationDto.getFirstName());
        person.setLastName(registrationDto.getLastName());
        person.setPhone(registrationDto.getPhone());
        person.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        person.setRoles(Collections.singleton(Role.USER));

        personRepository.save(person);

        return true;
    }
}
