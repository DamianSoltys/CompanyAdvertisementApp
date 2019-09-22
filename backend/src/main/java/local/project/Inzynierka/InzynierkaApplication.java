package local.project.Inzynierka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@EnableJpaRepositories(basePackages = "local.project.Inzynierka.persistence")
@SpringBootApplication
//@EntityScan(basePackages = "local.project.Inzynierka.persistence")
public class InzynierkaApplication {

	public static void main(String[] args) {
		SpringApplication.run(InzynierkaApplication.class, args);
	}

}
