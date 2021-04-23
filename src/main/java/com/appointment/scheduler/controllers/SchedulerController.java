package com.appointment.scheduler.controllers;

import com.appointment.scheduler.data.AppointmentService;
import com.appointment.scheduler.models.Appointment;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.joda.time.DateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SchedulerController {

    @ApiOperation("Takes a user ID (required) and returns all appointments for the user")
    @RequestMapping(value = "api/user/appointment", method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<Appointment>> getAppointmentsByUserEmail (
            @ApiParam("User Email") @RequestParam("userEmail") String userEmail){
        List<Appointment> result = AppointmentService.getAppointmentByUser(userEmail);

        if(result.size() == 0)
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        return ResponseEntity.ok().body(result);
    }

    @ApiOperation("Creates an appointment beginning at that time for that user")
    @RequestMapping(value= "api/user/appointment/", method = RequestMethod.POST)
    public ResponseEntity<String> createAppointmentForUser(
            @ApiParam("User Email") @RequestParam("userEmail") String userEmail,
            @ApiParam("Appointment Start Date Time ISO-Format") @RequestParam("dateTime") String dateTime){

        try{
            DateTime requestDateTime =  DateTime.parse(dateTime);

            //check if before current time
            if(requestDateTime.isBefore(DateTime.now()))
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Failed to create appointment: Date time is too old");

            Appointment result = AppointmentService.addAppointment(userEmail, requestDateTime);
            if (result != null)
                return ResponseEntity.ok().build();
            else
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body("Failed to create appointment because the user already has an appointment that day");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to create appointment: Date time format is incorrect");
        }

    }
}
