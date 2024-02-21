package com.swiftcart.swiftcart.reponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogInResponse {

    private String jwt;
    private String message;
    private LocalDateTime timeStamp;
}
