package com.ceir.CEIRPostman.service;

import java.io.IOException;

public interface SmsManagementService {
    String sendSms(String to, String from, String message, String correlationId, String msgLang);
}