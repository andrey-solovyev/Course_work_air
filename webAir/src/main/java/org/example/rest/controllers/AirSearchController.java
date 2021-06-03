package org.example.rest.controllers;

import org.example.data.dto.BookingInfoDto;
import org.example.data.dto.SearchAirDto;
import org.example.rest.services.AirSearchService;
import org.example.data.dto.BfmSearchAirDto;
import org.example.rest.services.ChooseAirToFlight;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;


@Controller
@RequestMapping("/air")
public class AirSearchController {
    private AirSearchService airSearchService;

    public AirSearchController(AirSearchService airSearchService) {
        this.airSearchService = airSearchService;
    }

    @GetMapping("/main")
    public String main(
            Model model
    ) {
        return "index";
    }

    @PostMapping("/main")
    public String searchFligth(@ModelAttribute BfmSearchAirDto bfmSearchAirDto, Model model) {
        var list = airSearchService.searchBfmOneWay(bfmSearchAirDto);
        model.addAttribute("searchAirDto", list);
        model.addAttribute("bfmSearchAirDto",bfmSearchAirDto);
        return "searchResult";
    }

    @GetMapping("/booking")
    public String search(Model model,@RequestParam int informationNumber) {
        ArrayList list = (ArrayList) model.getAttribute("searchAirDto");
        assert list != null;
        var info =(SearchAirDto) list.get(informationNumber);
        model.addAttribute("airDto", info);
        return "bookedAir";
    }

    @PostMapping("/choosing")
    public String chooseFlight(@ModelAttribute ChooseAirToFlight chooseAirToFlight,Model model){
        model.addAttribute("information",chooseAirToFlight);
        return "bookedAir";

    }

    @PostMapping("/booking")
    public String postSearch(@ModelAttribute BookingInfoDto bookingInfoDto,@ModelAttribute ChooseAirToFlight chooseAirToFlight, Model model) {
        airSearchService.bookingAir(bookingInfoDto,chooseAirToFlight);
        return "bookedAir";
    }

}
