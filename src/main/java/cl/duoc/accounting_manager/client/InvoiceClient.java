/*
 * Copyright © 2026 DuocUC FullStack 1
 * Eduardo Bray
 * Rodrigo Callealta
 * Fernando Villalobos
 */
package cl.duoc.accounting_manager.client;

import cl.duoc.accounting_manager.dto.request.invoice.InvoiceRequestDto;
import cl.duoc.accounting_manager.dto.response.invoice.InvoiceResponseDto;
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.annotation.PutExchange;

public interface InvoiceClient {

    String base = "/api/v1/invoices";

    @GetExchange(base)
    List<InvoiceResponseDto> getInvoices();

    @PostExchange(base)
    InvoiceResponseDto createInvoice(@RequestBody InvoiceRequestDto request);

    @PutExchange(base + "/{folio}/anular")
    InvoiceResponseDto anularInvoiceByFolio(@PathVariable Long folio);
}
