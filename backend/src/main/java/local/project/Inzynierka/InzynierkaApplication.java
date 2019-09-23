package local.project.Inzynierka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"local.project.Inzynierka",})
public class InzynierkaApplication {

	public static void main(String[] args) {
		SpringApplication.run(InzynierkaApplication.class, args);
	}

}
