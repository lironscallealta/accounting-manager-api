package cl.duoc.accounting_manager.dto.response.invoice;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceiptResponseDto {

    private Long id;
    private LocalDate fecha;
    private String folio;
    // datos receptor (cliente)
    private String direccionReceptor;
    private String giroReceptor;
    private String direcionReceptor;
    private String rutReceptor;
    // datos emisor(proveedor)
    private String razonSocialEmisor;
    private String giroEmisor;
    private String direccionEmisor;
    private String rutEmisor;

}
