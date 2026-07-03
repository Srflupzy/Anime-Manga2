package favoritos.cl.favoritos_service;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
@OpenAPIDefinition(
		info = @Info(
				title = "Favoritos API",
				description = "API para la gestión de favoritos, asociando usuarios con animes y evitando favoritos duplicados.",
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
public class FavoritosServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FavoritosServiceApplication.class, args);
	}

}
