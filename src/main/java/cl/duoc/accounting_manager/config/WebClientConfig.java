package cl.duoc.accounting_manager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration // Esta clase contiene configuración del contexto (beans)
public class WebClientConfig {

    @Bean // Ejecuta este método y guarda su resultado como un objeto administrado. La deja disponible para inyección (@Autowired o constructor)
    public WebClient webClientSales() {
        return WebClient.builder() // Crea un builder para configurar el cliente HTTP
                .baseUrl("http://localhost:8082/api/v1/sales") // Define la URL base del microservicio de ventas
                .defaultHeader("Content-Type", "application/json") // Agrega un header HTTP por defecto a TODAS las solicitudes
                .build(); // Construye el objeto final WebClient
    }

    @Bean // Ejecuta este método y guarda su resultado como un objeto administrado. La deja disponible para inyección (@Autowired o constructor)
    public WebClient webClientInvoice() {
        return WebClient.builder() // Crea un builder para configurar el cliente HTTP
                .baseUrl("http://localhost:8087/api/v1/invoices") // Define la URL base del microservicio de facturas
                .defaultHeader("Content-Type", "application/json") // Agrega un header HTTP por defecto a TODAS las solicitudes
                .build(); // Construye el objeto final WebClient
    }
}
