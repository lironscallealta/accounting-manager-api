package cl.duoc.accounting_manager.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import cl.duoc.accounting_manager.client.InvoiceClient;
import cl.duoc.accounting_manager.client.SalesClient;
import cl.duoc.accounting_manager.client.UsersClient;
import cl.duoc.accounting_manager.dto.request.AccountingCreateRequest;
import cl.duoc.accounting_manager.dto.request.invoice.InvoiceRequestDto;
import cl.duoc.accounting_manager.dto.request.sales.SaleCreationRequest;
import cl.duoc.accounting_manager.dto.response.AccountingCreateResponse;
import cl.duoc.accounting_manager.dto.response.AccountingResponse;
import cl.duoc.accounting_manager.dto.response.invoice.InvoiceResponseDto;
import cl.duoc.accounting_manager.dto.response.sales.SaleResponse;
import cl.duoc.accounting_manager.dto.response.users.UsuarioResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountingService {

    private final SalesClient salesClient;
    private final InvoiceClient invoiceClient;
    private final UsersClient usersClient;

    public AccountingResponse consultarCompraId(Long saleId) {
        log.info("Consultando compra por id: {}", saleId);

        SaleResponse venta = salesClient.findSale(saleId);
        List<InvoiceResponseDto> todasLasFacturas = invoiceClient.getInvoices();
        List<InvoiceResponseDto> facturasDeLaVenta = filtrarFacturasPorVenta(todasLasFacturas, saleId);

        AccountingResponse response = mapToAccountingResponse(venta, facturasDeLaVenta);
        return response;
    }

    public AccountingCreateResponse registrarCompra(AccountingCreateRequest request) {
        log.info("Registrando compra (venta + factura)");

        UsuarioResponseDto cliente = usersClient.findUserById(request.getCustomerId());
        log.info("Cliente validado en users-api: {} ({})", cliente.getNombreCompleto(), cliente.getRut());

        SaleCreationRequest ventaRequest = mapToSaleCreationRequest(request);
        SaleResponse venta = salesClient.saveSale(ventaRequest);

        try {
            InvoiceRequestDto facturaRequest = mapToInvoiceRequest(venta.getId(), request);
            InvoiceResponseDto factura = invoiceClient.createInvoice(facturaRequest);

            AccountingCreateResponse response = mapToAccountingCreateResponse(venta, factura);
            return response;
        } catch (RuntimeException ex) {
            log.warn("Error al crear factura, se revierte venta id: {}", venta.getId());
            salesClient.deleteSale(venta.getId());
            throw ex;
        }
    }

    private AccountingResponse mapToAccountingResponse(SaleResponse venta, List<InvoiceResponseDto> facturas) {
        AccountingResponse response = new AccountingResponse();
        response.setSale(venta);
        response.setInvoices(facturas);
        return response;
    }

    private AccountingCreateResponse mapToAccountingCreateResponse(SaleResponse venta, InvoiceResponseDto factura) {
        AccountingCreateResponse response = new AccountingCreateResponse();
        response.setSale(venta);
        response.setInvoice(factura);
        return response;
    }

    private SaleCreationRequest mapToSaleCreationRequest(AccountingCreateRequest request) {
        SaleCreationRequest ventaRequest = new SaleCreationRequest();
        ventaRequest.setCustomerId(request.getCustomerId());
        ventaRequest.setAmount(request.getAmount());
        ventaRequest.setDetails(request.getDetails());
        return ventaRequest;
    }

    private InvoiceRequestDto mapToInvoiceRequest(Long saleId, AccountingCreateRequest request) {
        InvoiceRequestDto facturaRequest = new InvoiceRequestDto();

        facturaRequest.setSaleId(saleId);
        facturaRequest.setEstado("ACTIVA");

        if (request.getFecha() == null) {
            facturaRequest.setFecha(LocalDate.now());
        } else {
            facturaRequest.setFecha(request.getFecha());
        }

        if (request.getFolio() == null || request.getFolio().isBlank()) {
            facturaRequest.setFolio("VENTA-" + saleId);
        } else {
            facturaRequest.setFolio(request.getFolio());
        }

        facturaRequest.setRazonSocialReceptor(request.getRazonSocialReceptor());
        facturaRequest.setGiroReceptor(request.getGiroReceptor());
        facturaRequest.setDireccionReceptor(request.getDireccionReceptor());
        facturaRequest.setRutReceptor(request.getRutReceptor());
        facturaRequest.setRazonSocialEmisor(request.getRazonSocialEmisor());
        facturaRequest.setGiroEmisor(request.getGiroEmisor());
        facturaRequest.setDireccionEmisor(request.getDireccionEmisor());
        facturaRequest.setRutEmisor(request.getRutEmisor());

        return facturaRequest;
    }

    private List<InvoiceResponseDto> filtrarFacturasPorVenta(
            List<InvoiceResponseDto> todasLasFacturas, Long saleId) {

        List<InvoiceResponseDto> facturasDeLaVenta = new ArrayList<>();

        for (InvoiceResponseDto factura : todasLasFacturas) {
            if (factura.getSaleId() != null && factura.getSaleId().equals(saleId)) {
                facturasDeLaVenta.add(factura);
            }
        }

        return facturasDeLaVenta;
    }
}
