package com.appointment.scheduler.data;

import com.appointment.scheduler.models.Appointment;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Setter
@Getter
public class AppointmentService {
    private static Map<String, List<Appointment>> userAppointmentMap = new HashMap<>();
    public static final String DATE_FORMAT = "MM/dd/yyyy HH:mm";

    public static Appointment addAppointment(String userEmail, DateTime start){
        // create appointment
        Appointment newAppointment = Appointment.builder().
                start(start).
                end(start.plusMinutes(30)).
                userEmail(userEmail).build();

        // check if user has made appointment before
        if(!userAppointmentMap.containsKey(userEmail)){
            List<Appointment> appointmentList = new ArrayList<>();
            appointmentList.add(newAppointment);
            userAppointmentMap.put(userEmail, appointmentList);
            return newAppointment;
        }

        // check if user can make an appointment with the requested time
        boolean isAvailable = true;
        for(Appointment appointment : userAppointmentMap.get(userEmail)){
            if(isSameDay(appointment.getStart(), newAppointment.getStart())){
                isAvailable = false;
                break;
            }
        }

        if(isAvailable) {
            userAppointmentMap.get(userEmail).add(newAppointment);
            return newAppointment;
        }
        return null;
    }

    public static List<Appointment> getAppointmentByUser(String userEmail){
        if(userAppointmentMap.containsKey(userEmail))
            return userAppointmentMap.get(userEmail);
        else
            return new ArrayList<>();
    }

    private static boolean isSameDay(DateTime date1, DateTime date2) {
        return date1.getDayOfYear() == date2.getDayOfYear();
    }
}
