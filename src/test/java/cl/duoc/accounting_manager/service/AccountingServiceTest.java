/*
 * Copyright © 2026 DuocUC FullStack 1
 * Eduardo Bray
 * Rodrigo Callealta
 * Fernando Villalobos
 */
package cl.duoc.accounting_manager.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cl.duoc.accounting_manager.client.InvoiceClient;
import cl.duoc.accounting_manager.client.SalesClient;
import cl.duoc.accounting_manager.client.UsersClient;
import cl.duoc.accounting_manager.dto.request.AccountingCreateRequest;
import cl.duoc.accounting_manager.dto.request.sales.SaleDetailRequest;
import cl.duoc.accounting_manager.dto.response.AccountingCreateResponse;
import cl.duoc.accounting_manager.dto.response.AccountingResponse;
import cl.duoc.accounting_manager.dto.response.invoice.InvoiceResponseDto;
import cl.duoc.accounting_manager.dto.response.sales.SaleResponse;
import cl.duoc.accounting_manager.dto.response.users.UsuarioResponseDto;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AccountingServiceTest {

    @Mock
    private SalesClient salesClient;

    @Mock
    private InvoiceClient invoiceClient;

    @Mock
    private UsersClient usersClient;

    @InjectMocks
    private AccountingService accountingService;

    private AccountingCreateRequest request;
    private SaleResponse saleResponse;
    private InvoiceResponseDto invoiceResponse;

    @BeforeEach
    void setUp() {
        request = new AccountingCreateRequest();
        request.setCustomerId(1L);
        request.setAmount(15000);
        request.setDetails(List.of(new SaleDetailRequest("Alimento", "SKU-001", 2, 7500)));
        request.setFolio("BOL-001");

        saleResponse = SaleResponse.builder()
                .id(10L)
                .customerId(1L)
                .amount(15000)
                .status("ACTIVE")
                .build();

        invoiceResponse = new InvoiceResponseDto();
        invoiceResponse.setId(20L);
        invoiceResponse.setSaleId(10L);
        invoiceResponse.setFolio(100L);
    }

    @Test
    void listarComprasDebeDelegarEnSalesClient() {
        when(salesClient.findAllSales()).thenReturn(List.of(saleResponse));

        List<SaleResponse> resultado = accountingService.listarCompras();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getId()).isEqualTo(10L);
        verify(salesClient).findAllSales();
    }

    @Test
    void consultarCompraIdDebeRetornarVentaYFacturas() {
        when(salesClient.findSale(10L)).thenReturn(saleResponse);
        when(invoiceClient.getInvoices()).thenReturn(List.of(invoiceResponse));

        AccountingResponse resultado = accountingService.consultarCompraId(10L);

        assertThat(resultado.getSale().getId()).isEqualTo(10L);
        assertThat(resultado.getInvoices()).hasSize(1);
    }

    @Test
    void registrarCompraDebeCrearVentaYFactura() {
        UsuarioResponseDto cliente =
                new UsuarioResponseDto(1L, "Cliente Demo", "12345678-9", "demo@mail.cl", 30, null, null, true);

        when(usersClient.findUserById(1L)).thenReturn(cliente);
        when(salesClient.saveSale(any())).thenReturn(saleResponse);
        when(invoiceClient.createInvoice(any())).thenReturn(invoiceResponse);

        AccountingCreateResponse resultado = accountingService.registrarCompra(request);

        assertThat(resultado.getSale().getId()).isEqualTo(10L);
        assertThat(resultado.getInvoice().getFolio()).isEqualTo(100L);
        verify(usersClient).findUserById(1L);
        verify(salesClient).saveSale(any());
        verify(invoiceClient).createInvoice(any());
    }

    @Test
    void registrarCompraDebeRevertirVentaCuandoFacturaFalla() {
        UsuarioResponseDto cliente =
                new UsuarioResponseDto(1L, "Cliente Demo", "12345678-9", "demo@mail.cl", 30, null, null, true);

        when(usersClient.findUserById(1L)).thenReturn(cliente);
        when(salesClient.saveSale(any())).thenReturn(saleResponse);
        when(invoiceClient.createInvoice(any())).thenThrow(new RuntimeException("Error en invoice-api"));

        assertThatThrownBy(() -> accountingService.registrarCompra(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("invoice-api");

        verify(salesClient).deleteSale(10L);
    }

    @Test
    void anularCompraDebeAnularFacturasYVenta() {
        InvoiceResponseDto facturaActiva = new InvoiceResponseDto();
        facturaActiva.setId(20L);
        facturaActiva.setSaleId(10L);
        facturaActiva.setFolio(100L);
        facturaActiva.setAnulada(false);

        SaleResponse ventaAnulada = SaleResponse.builder()
                .id(10L)
                .customerId(1L)
                .amount(15000)
                .status("DELETED")
                .build();

        when(salesClient.findSale(10L)).thenReturn(saleResponse, ventaAnulada);
        when(invoiceClient.getInvoices()).thenReturn(List.of(facturaActiva), List.of(facturaActiva));

        AccountingResponse resultado = accountingService.anularCompra(10L);

        assertThat(resultado.getSale().getId()).isEqualTo(10L);
        verify(invoiceClient).anularInvoiceByFolio(100L);
        verify(salesClient).deleteSale(10L);
    }

    @Test
    void anularCompraNoDebeAnularFacturaYaAnulada() {
        InvoiceResponseDto facturaAnulada = new InvoiceResponseDto();
        facturaAnulada.setId(20L);
        facturaAnulada.setSaleId(10L);
        facturaAnulada.setFolio(100L);
        facturaAnulada.setAnulada(true);

        when(salesClient.findSale(10L)).thenReturn(saleResponse);
        when(invoiceClient.getInvoices()).thenReturn(List.of(facturaAnulada));

        accountingService.anularCompra(10L);

        verify(invoiceClient, never()).anularInvoiceByFolio(100L);
        verify(salesClient).deleteSale(10L);
    }
}
