package local.project.Inzynierka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"local.project.Inzynierka.web",
                                           "local.project.Inzynierka.shared.",
                                           "local.project.Inzynierka.auth.",
                                           "local.project.Inzynierka.servicelayer.",
                                           "local.project.Inzynierka.persistence"})
public class InzynierkaApplication {

	public static void main(String[] args) {
		SpringApplication.run(InzynierkaApplication.class, args);
	}

}
