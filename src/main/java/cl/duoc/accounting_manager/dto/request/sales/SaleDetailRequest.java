/*
 * Copyright © 2026 DuocUC FullStack 1
 * Eduardo Bray
 * Rodrigo Callealta
 * Fernando Villalobos
 */
package cl.duoc.accounting_manager.dto.request.sales;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleDetailRequest {
    @NotBlank(message = "La descripción no debe estar en blanco")
    private String description;

    @NotBlank(message = "El identificator SKU es obligatorio")
    private String sku;

    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad del producto no puede ser negativa")
    private Integer quantity;

    @NotNull(message = "El precio unitario es obligatorio")
    @PositiveOrZero(message = "El precio no puede ser negativo")
    private Integer unitPrice;
}
