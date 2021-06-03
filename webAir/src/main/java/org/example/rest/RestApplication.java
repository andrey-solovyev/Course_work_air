package org.example.rest;

import org.example.contractAir.bargainfindermax.OTAAirLowFareSearchRS;
import org.example.data.dto.BfmSearchAirDto;
import org.example.request.services.AirService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@SpringBootApplication(scanBasePackages = "org.example")
@EnableJpaRepositories("org.example.data")
@EntityScan("org.example.data")
public class RestApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestApplication.class, args);
        //System.out.println(otaAirLowFareSearchRS.getAvailableItineraries().getPricedItineraries().getPricedItinerary().size());
    }
}
/*
    @startuml
    package "Course Work" {
    component course_work
    component contractAir
    component data
    component request
    component configuration
    component webAir
    component workflow
    component errorhandler
    course_work --> contractAir
    course_work --> configuration
    course_work --> data
    course_work --> workflow
    course_work --> request
    course_work --> webAir
    course_work --> errorhandler
    request --> contractAir
    request --> errorhandler
    request --> configuration
    request --> data
    request --> workflow
    webAir --> request
    webAir --> data
    errorhandler --> workflow
@enduml

 */

/*
@startuml
package "Example"{
    component webAir
    component data
    component request
    component configuration
    component workflow
    component errorhandler
    component contractAir

@enduml
 */