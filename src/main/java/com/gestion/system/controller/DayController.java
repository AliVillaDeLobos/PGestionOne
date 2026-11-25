package com.gestion.system.controller;

import com.gestion.system.dto.response.DayResponse;
import com.gestion.system.service.DayService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/days")
@RequiredArgsConstructor
@PreAuthorize("hasRole('MEMBER')")
public class DayController {
    private final DayService dayService;

    @GetMapping("/{id}")
    public ResponseEntity<DayResponse> getDayById(@PathVariable Integer id) {
        DayResponse response = dayService.getDayResponseById(id);
        return ResponseEntity.ok(response);
    }

     @GetMapping("/week/{id}")
    public ResponseEntity<List<DayResponse>> getAllByWeek(@PathVariable Integer id){
        List<DayResponse> dayResponses = dayService.getDaysByWeek(id);
            return ResponseEntity.ok(dayResponses);
     }

     @GetMapping("/week/{idWeek}/{nameDay}")
    public ResponseEntity<DayResponse> getByWeekIdAndNameDay(@PathVariable Integer idWeek, @PathVariable String nameDay) {
        DayResponse response = dayService.getDayByNameAndWeek_Id(nameDay, idWeek);
        return ResponseEntity.ok(response);
     }

}
