package kadri.rizq_platform.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService implements EmailSender {

    private final JavaMailSender mailSender;
    
    public void sendWelcomeEmail(String to, String username, String password) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("🎉 Welcome to Rezq Platform");
        message.setText("""
                Your account has been approved ✅

                Username: %s
                Temporary Password: %s

                Please log in.
                """.formatted(username, password));

        mailSender.send(message);
    }
}

