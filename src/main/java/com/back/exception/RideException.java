package com.back.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


public class RideException extends Exception{

        public RideException(String message) {
            super(message);
        }

        public RideException(String message, Throwable cause) {
            super(message, cause);
        }
}
