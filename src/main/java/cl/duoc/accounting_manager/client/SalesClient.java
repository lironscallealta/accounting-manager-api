/*
 * Copyright © 2026 DuocUC FullStack 1
 * Eduardo Bray
 * Rodrigo Callealta
 * Fernando Villalobos
 */
package cl.duoc.accounting_manager.client;

import cl.duoc.accounting_manager.dto.request.sales.SaleCreationRequest;
import cl.duoc.accounting_manager.dto.response.sales.SaleResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

public interface SalesClient {

    String base = "/api/v1/sales";

    @GetExchange(base + "/{id}")
    SaleResponse findSale(@PathVariable Long id);

    @PostExchange(base)
    SaleResponse saveSale(@RequestBody SaleCreationRequest req);

    @DeleteExchange(base + "/{id}")
    void deleteSale(@PathVariable Long id);
}
