package com.unihack.smart_usb.api.error_handling;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MainErrorResponse {
    int statusCode;
    String reasonPhrase;
    String message;
    String code;
    String identifier;
}
