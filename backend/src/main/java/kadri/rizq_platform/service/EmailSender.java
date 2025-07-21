package kadri.rizq_platform.service;

public interface EmailSender {
    void sendWelcomeEmail(String to, String username, String password);
}
