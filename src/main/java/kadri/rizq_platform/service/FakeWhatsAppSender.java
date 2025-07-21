package kadri.rizq_platform.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FakeWhatsAppSender implements MessageSender{
    @Override
    public void send(String phoneNumber, String message) {
        log.info("📤 [Fake WhatsApp] Sending to {}:\n{}", phoneNumber, message);
    }
}
