/*
 * Copyright © 2026 DuocUC FullStack 1
 * Eduardo Bray
 * Rodrigo Callealta
 * Fernando Villalobos
 */
package cl.duoc.accounting_manager.config;

import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.rewritePath;
import static org.springframework.cloud.gateway.server.mvc.filter.FilterFunctions.uri;
import static org.springframework.cloud.gateway.server.mvc.filter.LoadBalancerFilterFunctions.lb;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.web.servlet.function.RequestPredicates.path;

import java.net.URI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.HandlerFilterFunction;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

@Slf4j
@Configuration
public class GatewayConfig {

    @Value("${app.service.invoices.base-url}")
    private String invoicesBaseUrl;

    @Value("${app.service.sales.base-url}")
    private String salesBaseUrl;

    @Bean
    public RouterFunction<ServerResponse> domainRoutes() {
        return route("invoices_passthrough")
                .route(path("/api/v1/invoices/**"), http())
                .filter(getRoutingFilter(invoicesBaseUrl))
                .build()
                .and(route("sales_passthrough")
                        .route(path("/api/v1/sales/**"), http())
                        .filter(getRoutingFilter(salesBaseUrl))
                        .build())
                .and(route("invoices_docs")
                        .route(path("/invoices/v3/api-docs"), http())
                        .before(rewritePath("/invoices/(?<segment>.*)", "/${segment}"))
                        .filter(getRoutingFilter(invoicesBaseUrl))
                        .build())
                .and(route("sales_docs")
                        .route(path("/sales/v3/api-docs"), http())
                        .before(rewritePath("/sales/(?<segment>.*)", "/${segment}"))
                        .filter(getRoutingFilter(salesBaseUrl))
                        .build());
    }

    private HandlerFilterFunction<ServerResponse, ServerResponse> getRoutingFilter(String url) {
        log.info("Routing URL: '{}'", url);
        return isLocalURL(url) ? uri(url) : lb(URI.create(url).getHost());
    }

    private boolean isLocalURL(String url) {
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("Missing domain service url");
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
            throw new IllegalArgumentException("Bad domain service url: " + url);
        }
    }
}
