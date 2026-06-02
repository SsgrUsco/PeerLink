package co.edu.usco.peerlink;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada de la aplicacion Spring Boot de PeerLink.
 *
 * <p>Inicializa el backend REST, la configuracion de seguridad, persistencia
 * con PostgreSQL, Swagger/OpenAPI, JasperReports y los recursos estaticos del
 * frontend.</p>
 */
@SpringBootApplication
public class PeerlinkApplication {

	/**
	 * Arranca el contexto principal de Spring Boot.
	 *
	 * @param args argumentos recibidos desde la linea de comandos
	 */
	public static void main(String[] args) {
		SpringApplication.run(PeerlinkApplication.class, args);
	}

}
