/*
 * Copyright © 2026 DuocUC FullStack 1
 * Eduardo Bray
 * Rodrigo Callealta
 * Fernando Villalobos
 */
package cl.duoc.accounting_manager.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cl.duoc.accounting_manager.dto.request.AccountingCreateRequest;
import cl.duoc.accounting_manager.dto.request.sales.SaleDetailRequest;
import cl.duoc.accounting_manager.dto.response.AccountingCreateResponse;
import cl.duoc.accounting_manager.dto.response.AccountingResponse;
import cl.duoc.accounting_manager.dto.response.invoice.InvoiceResponseDto;
import cl.duoc.accounting_manager.dto.response.sales.SaleResponse;
import cl.duoc.accounting_manager.exception.GlobalExceptionHandler;
import cl.duoc.accounting_manager.exception.ResourceNotFoundException;
import cl.duoc.accounting_manager.security.JwtAuthFilter;
import cl.duoc.accounting_manager.service.AccountingService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.json.JsonMapper;

@WebMvcTest(CompraController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
@ActiveProfiles("test")
class CompraControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper jsonMapper;

    @MockitoBean
    private AccountingService accountingService;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    @Test
    void consultarCompraIdDebeRetornar200() throws Exception {
        SaleResponse sale =
                SaleResponse.builder().id(5L).customerId(1L).amount(10000).build();
        AccountingResponse response = new AccountingResponse();
        response.setSale(sale);
        response.setInvoices(List.of());

        when(accountingService.consultarCompraId(5L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/compras/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sale.id").value(5));
    }

    @Test
    void registrarCompraDebeRetornar201CuandoRequestEsValido() throws Exception {
        AccountingCreateRequest request = new AccountingCreateRequest();
        request.setCustomerId(1L);
        request.setAmount(15000);
        request.setDetails(List.of(new SaleDetailRequest("Alimento", "SKU-001", 2, 7500)));

        SaleResponse sale =
                SaleResponse.builder().id(10L).customerId(1L).amount(15000).build();
        InvoiceResponseDto invoice = new InvoiceResponseDto();
        invoice.setId(20L);
        invoice.setFolio(100L);

        AccountingCreateResponse response = new AccountingCreateResponse();
        response.setSale(sale);
        response.setInvoice(invoice);

        when(accountingService.registrarCompra(any(AccountingCreateRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/v1/compras")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.sale.id").value(10))
                .andExpect(jsonPath("$.invoice.folio").value(100));

        verify(accountingService).registrarCompra(any(AccountingCreateRequest.class));
    }

    @Test
    void registrarCompraDebeRetornar400CuandoCustomerIdEsNull() throws Exception {
        AccountingCreateRequest request = new AccountingCreateRequest();
        request.setAmount(15000);
        request.setDetails(List.of(new SaleDetailRequest("Alimento", "SKU-001", 2, 7500)));

        mockMvc.perform(post("/api/v1/compras")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(accountingService, never()).registrarCompra(any(AccountingCreateRequest.class));
    }

    @Test
    void consultarCompraIdDebeRetornar404CuandoNoExiste() throws Exception {
        when(accountingService.consultarCompraId(99L))
                .thenThrow(new ResourceNotFoundException("Compra no encontrada con id 99"));

        mockMvc.perform(get("/api/v1/compras/99")).andExpect(status().isNotFound());
    }

    @Test
    void anularCompraDebeRetornar200() throws Exception {
        SaleResponse sale = SaleResponse.builder()
                .id(5L)
                .customerId(1L)
                .amount(10000)
                .status("DELETED")
                .build();
        AccountingResponse response = new AccountingResponse();
        response.setSale(sale);
        response.setInvoices(List.of());

        when(accountingService.anularCompra(5L)).thenReturn(response);

        mockMvc.perform(patch("/api/v1/compras/5/anular"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sale.id").value(5));

        verify(accountingService).anularCompra(5L);
    }
}
