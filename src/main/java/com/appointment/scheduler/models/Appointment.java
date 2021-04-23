package com.appointment.scheduler.models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;

@Data
@Getter
@Setter
@Builder
public class Appointment {
    private String userEmail;

    @JsonSerialize(using = JsonJodaDateTimeSerializer.class)
    private DateTime start;

    @JsonSerialize(using = JsonJodaDateTimeSerializer.class)
    private DateTime end;
}
