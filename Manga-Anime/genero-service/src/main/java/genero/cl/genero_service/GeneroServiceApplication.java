package genero.cl.genero_service;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@OpenAPIDefinition(
		info = @Info(
				title = "Género API",
				description = "API para la gestión de géneros de anime y manga.",
				version = "1.0.1",
				contact = @Contact(
						name = "Equipo Anime",
						email = "osca.rocha@duouc.cl"
				)
		)
)
@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
public class GeneroServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GeneroServiceApplication.class, args);
	}

}
