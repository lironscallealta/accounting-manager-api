/*
 * Copyright © 2026 DuocUC FullStack 1
 * Eduardo Bray
 * Rodrigo Callealta
 * Fernando Villalobos
 */
package cl.duoc.accounting_manager.client;

import cl.duoc.accounting_manager.dto.request.sales.SaleCreationRequest;
import cl.duoc.accounting_manager.dto.request.sales.SaleUpdateRequest;
import cl.duoc.accounting_manager.dto.response.sales.SaleResponse;
import cl.duoc.accounting_manager.dto.response.sales.SaleStatusResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class SalesClient {

    private final WebClient webClientSales;

    public SaleResponse findSale(Long id) {

        return webClientSales
                .get()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(SaleResponse.class)
                .block();
    }

    public List<SaleResponse> findAllSales() {

        return webClientSales
                .get()
                .retrieve()
                .bodyToFlux(SaleResponse.class)
                .collectList()
                .block();
    }

    public SaleResponse saveSale(SaleCreationRequest req) {

        return webClientSales
                .post()
                .bodyValue(req)
                .retrieve()
                .bodyToMono(SaleResponse.class)
                .block();
    }

    public SaleResponse replaceSale(Long id, SaleUpdateRequest req) {

        return webClientSales
                .put()
                .uri("/{id}", id)
                .bodyValue(req)
                .retrieve()
                .bodyToMono(SaleResponse.class)
                .block();
    }

    public void deleteSale(Long id) {

        webClientSales.delete().uri("/{id}", id).retrieve().toBodilessEntity().block();
    }

    public List<SaleStatusResponse> findAllSaleStatus() {

        return webClientSales
                .get()
                .uri("/status")
                .retrieve()
                .bodyToFlux(SaleStatusResponse.class)
                .collectList()
                .block();
    }
}
