package org.unibl.etf.emobility_hub.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.unibl.etf.emobility_hub.models.projection.DailyRevenueProjection;
import org.unibl.etf.emobility_hub.models.projection.FaultCountProjection;
import org.unibl.etf.emobility_hub.models.projection.RevenueByVehicleTypeProjection;
import org.unibl.etf.emobility_hub.services.IStatisticService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/statistics")
public class StatisticController {
    @Autowired
    private IStatisticService statisticService;

    @GetMapping("/getDailyRevenue")
    public ResponseEntity<List<DailyRevenueProjection>> getDailyRevenue(@RequestParam("start") String start, @RequestParam("end") String end) {
        LocalDateTime startDateTime = LocalDateTime.parse(start);
        LocalDateTime endDateTime = LocalDateTime.parse(end);
        List<DailyRevenueProjection> response = statisticService.getDailyRevenue(startDateTime, endDateTime);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/getRevenueByVehicleType")
    public ResponseEntity<List<RevenueByVehicleTypeProjection>> getRevenueByVehicleType(){
        List<RevenueByVehicleTypeProjection> response = statisticService.getRevenueByVehicleType();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/getFaultsPerVehicle")
    public ResponseEntity<List<FaultCountProjection>> countFaultsPerVehicle(){
        List<FaultCountProjection> response = statisticService.countFaultsPerVehicle();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
