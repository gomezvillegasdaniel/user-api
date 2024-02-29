package com.example.demo.data.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HttpResponse {

    @JsonFormat(
        shape = JsonFormat.Shape.STRING,
        pattern = "MM-dd-yyyy hh:mm:ss",
        timezone = "America/Mexico_City"
    )
    private Date timeStamp;
    private int httpStatusCode;
    private HttpStatus httpStatus;
    private String reason;
    private String message;

}
