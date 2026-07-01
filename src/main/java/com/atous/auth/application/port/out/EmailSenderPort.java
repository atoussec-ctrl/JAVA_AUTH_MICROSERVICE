package com.atous.auth.application.port.out;

public interface EmailSenderPort { void sendPasswordResetEmail(String to, String resetToken); void sendWelcomeEmail(String to, String name); }
