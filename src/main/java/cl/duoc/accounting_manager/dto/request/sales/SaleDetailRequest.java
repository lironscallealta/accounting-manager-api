/*
 * Copyright © 2026 DuocUC FullStack 1
 * Eduardo Bray
 * Rodrigo Callealta
 * Fernando Villalobos
 */
package cl.duoc.accounting_manager.dto.request.sales;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Línea de detalle de una venta")
public class SaleDetailRequest {
    @NotBlank(message = "La descripción no debe estar en blanco")
    @Schema(
            description = "Descripción del ítem",
            example = "Alimento premium",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String description;

    @NotBlank(message = "El identificator SKU es obligatorio")
    @Schema(description = "SKU del producto", example = "SKU-001", requiredMode = Schema.RequiredMode.REQUIRED)
    private String sku;

    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad del producto no puede ser negativa")
    @Schema(description = "Cantidad vendida", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer quantity;

    @NotNull(message = "El precio unitario es obligatorio")
    @PositiveOrZero(message = "El precio no puede ser negativo")
    @Schema(description = "Precio unitario", example = "7500", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer unitPrice;
}
