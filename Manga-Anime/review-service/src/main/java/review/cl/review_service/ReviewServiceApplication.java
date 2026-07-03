package review.cl.review_service;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
@OpenAPIDefinition(
		info = @Info(
				title = "Review API",
				description = "API para la gestión de reviews, mostrando el usuario que escribe la reseña y el anime evaluado.",
				version = "1.0.1",
				contact = @Contact(
						name = "Equipo Anime",
						email = "osca.rocha@duouc.cl"
				)
		)
)
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class ReviewServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReviewServiceApplication.class, args);
	}

}
