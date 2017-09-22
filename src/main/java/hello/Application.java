package hello;

import kamon.Kamon;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application{

	public static void main(String[] args) {
    Kamon.start();
		SpringApplication.run(Application.class, args);
	}
}