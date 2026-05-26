package cl.duoc.accounting_manager.dto.response.invoice;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceResponseDto {

    private Long id;
    private LocalDate fecha;
    private String folio;
    private Long saleId;
    private String estado;
    // datos receptor (cliente)
    private String razonSocialReceptor;
    private String giroReceptor;
    private String direccionReceptor;
    private String rutReceptor;
    // datos emisor(proveedor)
    private String razonSocialEmisor;
    private String giroEmisor;
    private String direccionEmisor;
    private String rutEmisor;

}
