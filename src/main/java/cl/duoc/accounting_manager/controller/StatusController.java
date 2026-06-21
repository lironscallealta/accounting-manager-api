/*
 * Copyright © 2026 DuocUC FullStack 1
 * Eduardo Bray
 * Rodrigo Callealta
 * Fernando Villalobos
 */
package cl.duoc.accounting_manager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Health", description = "Estado del microservicio.")
public class StatusController {

    @GetMapping("/api/v1/health")
    @Operation(summary = "Health check", description = "Verifica que el microservicio esté disponible.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Microservicio operativo"),
        @ApiResponse(responseCode = "503", description = "Microservicio no disponible")
    })
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OK");
    }
}
