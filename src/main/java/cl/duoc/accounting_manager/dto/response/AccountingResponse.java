package cl.duoc.accounting_manager.dto.response;

import java.util.List;

import cl.duoc.accounting_manager.dto.response.invoice.InvoiceResponseDto;
import cl.duoc.accounting_manager.dto.response.sales.SaleResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Respuesta de "ver compra": venta en sales-api + facturas vinculadas en invoice-api (por saleId).
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountingResponse {

    private SaleResponse sale;
    private List<InvoiceResponseDto> invoices;
}
