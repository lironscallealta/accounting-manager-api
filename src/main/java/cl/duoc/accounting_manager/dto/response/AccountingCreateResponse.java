/*
 * Copyright © 2026 DuocUC FullStack 1
 * Eduardo Bray
 * Rodrigo Callealta
 * Fernando Villalobos
 */
package cl.duoc.accounting_manager.dto.response;

import cl.duoc.accounting_manager.dto.response.invoice.InvoiceResponseDto;
import cl.duoc.accounting_manager.dto.response.sales.SaleResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountingCreateResponse {

    private SaleResponse sale;
    private InvoiceResponseDto invoice;
}
