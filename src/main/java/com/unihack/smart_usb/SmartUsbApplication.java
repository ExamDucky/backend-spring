package com.unihack.smart_usb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class SmartUsbApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartUsbApplication.class, args);
    }

}
