/*
 * Copyright © 2026 DuocUC FullStack 1
 * Eduardo Bray
 * Rodrigo Callealta
 * Fernando Villalobos
 */
package cl.duoc.accounting_manager.dto.request.sales;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleCreationRequest {
    @NotNull(message = "La venta debe estar asociada a un cliente")
    @Positive(message = "La id del usuario no puede ser negativa")
    private Long customerId;

    @NotNull(message = "El valor de venta es obligatorio")
    @Positive(message = "El valor de venta no puede ser negativo")
    private Integer amount;

    @NotNull(message = "Los detalles de venta son obligatorios")
    @Valid
    private List<SaleDetailRequest> details;
}
