package co.edu.usco.peerlink;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Inicializador requerido cuando PeerLink se despliega como archivo WAR
 * en un contenedor Servlet externo.
 */
public class ServletInitializer extends SpringBootServletInitializer {

	/**
	 * Registra la clase principal de Spring Boot como fuente de configuracion.
	 *
	 * @param application constructor de la aplicacion Servlet
	 * @return constructor configurado con la aplicacion PeerLink
	 */
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(PeerlinkApplication.class);
	}

}
