package infsus.pampol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "infsus.pampol")
public class PampolApplication {

	public static void main(String[] args) {
		SpringApplication.run(PampolApplication.class, args);
	}

}
