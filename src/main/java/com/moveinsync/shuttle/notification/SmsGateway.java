package com.moveinsync.shuttle.notification;

/** Port for the SMS provider; swap the implementation (Twilio, SNS, ...) without touching business code. */
public interface SmsGateway {
    void send(String phone, String message);
}
