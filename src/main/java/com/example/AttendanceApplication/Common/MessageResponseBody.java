package com.example.AttendanceApplication.Common;

import lombok.NoArgsConstructor;
import org.springframework.context.MessageSource;

import java.util.Locale;

@NoArgsConstructor
public class MessageResponseBody {

    MessageSource messageSource;
    public MessageResponseBody(String MsgCode, String error){
        messageSource.getMessage(MsgCode, null, Locale.getDefault());
    }



}
