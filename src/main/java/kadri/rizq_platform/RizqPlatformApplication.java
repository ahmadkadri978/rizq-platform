package kadri.rizq_platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class RizqPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(RizqPlatformApplication.class, args);
	}

}
