package cl.duoc.accounting_manager.dto.request;

import java.time.LocalDate;
import java.util.List;

import cl.duoc.accounting_manager.dto.request.sales.SaleDetailRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountingCreateRequest {

    // customerId = id del usuario en users-api (se valida antes de crear la venta)
    @NotNull(message = "La venta debe estar asociada a un cliente")
    @Positive(message = "La id del cliente no puede ser negativa")
    private Long customerId;

    @NotNull(message = "El valor de venta es obligatorio")
    @Positive(message = "El valor de venta no puede ser negativo")
    private Integer amount;

    @NotEmpty(message = "Los detalles de venta son obligatorios")
    @Valid
    private List<SaleDetailRequest> details;

    // (invoice-api)
    private LocalDate fecha;
    private String folio;

    private String razonSocialReceptor;
    private String giroReceptor;
    private String direccionReceptor;
    private String rutReceptor;

    private String razonSocialEmisor;
    private String giroEmisor;
    private String direccionEmisor;
    private String rutEmisor;
}
