package cl.duoc.accounting_manager.dto.response;

import cl.duoc.accounting_manager.dto.response.invoice.InvoiceResponseDto;
import cl.duoc.accounting_manager.dto.response.sales.SaleResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Respuesta de "guardar / generar compra": lo creado en sales-api e invoice-api en una sola respuesta.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountingCreateResponse {

    private SaleResponse sale;
    private InvoiceResponseDto invoice;
}
