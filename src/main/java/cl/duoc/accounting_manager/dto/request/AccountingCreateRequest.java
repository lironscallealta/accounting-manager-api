/*
 * Copyright © 2026 DuocUC FullStack 1
 * Eduardo Bray
 * Rodrigo Callealta
 * Fernando Villalobos
 */
package cl.duoc.accounting_manager.dto.request;

import cl.duoc.accounting_manager.dto.request.sales.SaleDetailRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Datos para registrar una compra orquestada (venta + factura)")
public class AccountingCreateRequest {

    @NotNull(message = "La venta debe estar asociada a un cliente")
    @Positive(message = "La id del cliente no puede ser negativa")
    @Schema(description = "ID del cliente en users-api", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long customerId;

    @NotNull(message = "El valor de venta es obligatorio")
    @Positive(message = "El valor de venta no puede ser negativo")
    @Schema(description = "Monto total de la venta", example = "15000", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer amount;

    @NotEmpty(message = "Los detalles de venta son obligatorios")
    @Valid
    @Schema(description = "Detalle de productos o servicios vendidos", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<SaleDetailRequest> details;

    @Schema(description = "Fecha de emisión de la factura", example = "2026-06-21")
    private LocalDate fecha;

    @Schema(description = "Folio del documento tributario", example = "BOL-001")
    private String folio;

    @Schema(description = "Razón social del receptor", example = "Cliente Demo SpA")
    private String razonSocialReceptor;

    @Schema(description = "Giro comercial del receptor", example = "Comercio")
    private String giroReceptor;

    @Schema(description = "Dirección del receptor", example = "Av. Principal 123")
    private String direccionReceptor;

    @Schema(description = "RUT del receptor", example = "12345678-9")
    private String rutReceptor;

    @Schema(description = "Razón social del emisor", example = "VetDistribuidora SpA")
    private String razonSocialEmisor;

    @Schema(description = "Giro comercial del emisor", example = "Veterinaria")
    private String giroEmisor;

    @Schema(description = "Dirección del emisor", example = "Calle Secundaria 456")
    private String direccionEmisor;

    @Schema(description = "RUT del emisor", example = "87654321-K")
    private String rutEmisor;
}
