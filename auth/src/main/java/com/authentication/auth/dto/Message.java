package com.authentication.auth.dto;

import com.authentication.auth.constant.MessageStatus;

public class Message
{

    public String status;
    public String message;


    public Message(MessageStatus status, String message)
    {
        this.status = String.valueOf(status);
        this.message = message;
    }

    public Message()
    {
    }


}
