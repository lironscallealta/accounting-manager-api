/*
 * Copyright © 2026 DuocUC FullStack 1
 * Eduardo Bray
 * Rodrigo Callealta
 * Fernando Villalobos
 */
package cl.duoc.accounting_manager.controller;

import cl.duoc.accounting_manager.dto.request.AccountingCreateRequest;
import cl.duoc.accounting_manager.dto.response.AccountingCreateResponse;
import cl.duoc.accounting_manager.dto.response.AccountingResponse;
import cl.duoc.accounting_manager.dto.response.sales.SaleResponse;
import cl.duoc.accounting_manager.service.AccountingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/compras")
@RequiredArgsConstructor
@Tag(name = "Compras", description = "Casos de uso orquestados: venta + factura vía sales-api e invoice-api.")
public class CompraController {

    private final AccountingService accountingService;

    @GetMapping
    @Operation(summary = "Listar compras", description = "Historial de ventas desde sales-api.")
    public ResponseEntity<List<SaleResponse>> listarCompras() {
        return ResponseEntity.ok(accountingService.listarCompras());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Ver compra", description = "Venta en sales-api y facturas vinculadas en invoice-api.")
    public ResponseEntity<AccountingResponse> consultarCompraId(@PathVariable Long id) {
        return ResponseEntity.ok(accountingService.consultarCompraId(id));
    }

    @PostMapping
    @Operation(
            summary = "Registrar compra",
            description = "Crea venta y factura. Si falla la factura, revierte la venta.")
    public ResponseEntity<AccountingCreateResponse> registrarCompra(
            @Valid @RequestBody AccountingCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountingService.registrarCompra(request));
    }

    @PatchMapping("/{id}/anular")
    @Operation(
            summary = "Anular compra",
            description = "Anulación lógica: venta (soft delete) y factura(s) asociada(s).")
    public ResponseEntity<AccountingResponse> anularCompra(@PathVariable Long id) {
        return ResponseEntity.ok(accountingService.anularCompra(id));
    }
}
