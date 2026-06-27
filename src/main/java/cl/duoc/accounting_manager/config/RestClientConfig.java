/*
 * Copyright © 2026 DuocUC FullStack 1
 * Eduardo Bray
 * Rodrigo Callealta
 * Fernando Villalobos
 */
package cl.duoc.accounting_manager.config;

import cl.duoc.accounting_manager.client.InvoiceClient;
import cl.duoc.accounting_manager.client.SalesClient;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Slf4j
@Configuration
public class RestClientConfig {

    @Value("${app.service.invoices.base-url}")
    private String invoicesBaseUrl;

    @Value("${app.service.sales.base-url}")
    private String salesBaseUrl;

    @Bean
    @Primary
    public RestClient.Builder defaultRestClientBuilder() {
        return RestClient.builder();
    }

    @Bean
    @LoadBalanced
    public RestClient.Builder loadBalancedRestClientBuilder() {
        return RestClient.builder();
    }

    @Bean
    public InvoiceClient invoiceClient() {
        RestClient client = resolveBuilder(invoicesBaseUrl)
                .baseUrl(invoicesBaseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .requestInterceptor(tokenPropagationInterceptor())
                .build();

        return HttpServiceProxyFactory.builderFor(RestClientAdapter.create(client))
                .build()
                .createClient(InvoiceClient.class);
    }

    @Bean
    public SalesClient salesClient() {
        RestClient client = resolveBuilder(salesBaseUrl)
                .baseUrl(salesBaseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .requestInterceptor(tokenPropagationInterceptor())
                .requestInterceptor((request, body, execution) -> {
                    log.debug("Request: {} {} | Body: {}", request.getMethod(), request.getURI(), new String(body));
                    return execution.execute(request, body);
                })
                .build();

        return HttpServiceProxyFactory.builderFor(RestClientAdapter.create(client))
                .build()
                .createClient(SalesClient.class);
    }

    private ClientHttpRequestInterceptor tokenPropagationInterceptor() {
        return new ClientHttpRequestInterceptor() {
            @Override
            public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
                    throws IOException {
                ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                if (attrs != null) {
                    HttpServletRequest servletRequest = attrs.getRequest();
                    String authHeader = servletRequest.getHeader(HttpHeaders.AUTHORIZATION);
                    if (authHeader != null && authHeader.startsWith("Bearer ")) {
                        request.getHeaders().add(HttpHeaders.AUTHORIZATION, authHeader);
                    }
                }
                return execution.execute(request, body);
            }
        };
    }

    private RestClient.Builder resolveBuilder(String url) {
        return isLocalURL(url)
                ? RestClient.builder()
                : loadBalancedRestClientBuilder().clone();
    }

    private boolean isLocalURL(String url) {
        if (url == null || url.isBlank()) {
            return false;
        }

        try {
            String host = URI.create(url).getHost();
            return host != null
                    && (!url.startsWith("lb://"))
                    && (host.equalsIgnoreCase("localhost")
                            || host.equals("127.0.0.1")
                            || host.equals("[::1]")
                            || host.equals("0:0:0:0:0:0:0:1"));
        } catch (IllegalArgumentException err) {
            throw new IllegalArgumentException("Bad downstream service url: " + url);
        }
    }
}
