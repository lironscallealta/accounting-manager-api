package cl.duoc.accounting_manager.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import cl.duoc.accounting_manager.client.InvoiceClient;
import cl.duoc.accounting_manager.client.SalesClient;
import cl.duoc.accounting_manager.dto.request.AccountingCreateRequest;
import cl.duoc.accounting_manager.dto.request.invoice.InvoiceRequestDto;
import cl.duoc.accounting_manager.dto.request.sales.SaleCreationRequest;
import cl.duoc.accounting_manager.dto.response.AccountingCreateResponse;
import cl.duoc.accounting_manager.dto.response.AccountingResponse;
import cl.duoc.accounting_manager.dto.response.invoice.InvoiceResponseDto;
import cl.duoc.accounting_manager.dto.response.sales.SaleResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountingService {

    private final SalesClient salesClient;
    private final InvoiceClient invoiceClient;

    public AccountingResponse verCompra(Long saleId) {
        log.info("Consultando compra id: {}", saleId);

        SaleResponse sale = salesClient.findSale(saleId);
        List<InvoiceResponseDto> invoices = filtrarFacturasPorVenta(invoiceClient.getInvoices(), saleId);

        return armarAccountingResponse(sale, invoices);
    }

    public AccountingCreateResponse guardarCompra(AccountingCreateRequest request) {
        log.info("Generando compra (venta + factura)");

        SaleCreationRequest saleRequest = armarSaleCreationRequest(request);
        SaleResponse sale = salesClient.saveSale(saleRequest);

        try {
            InvoiceRequestDto invoiceRequest = armarInvoiceRequest(sale.getId(), request);
            InvoiceResponseDto invoice = invoiceClient.createInvoice(invoiceRequest);
            return armarAccountingCreateResponse(sale, invoice);
        } catch (RuntimeException ex) {
            log.warn("Error al crear factura, se revierte venta id: {}", sale.getId());
            salesClient.deleteSale(sale.getId());
            throw ex;
        }
    }

    private SaleCreationRequest armarSaleCreationRequest(AccountingCreateRequest request) {
        SaleCreationRequest saleRequest = new SaleCreationRequest();
        saleRequest.setCustomerId(request.getCustomerId());
        saleRequest.setAmount(request.getAmount());
        saleRequest.setDetails(request.getDetails());
        return saleRequest;
    }

    private InvoiceRequestDto armarInvoiceRequest(Long saleId, AccountingCreateRequest request) {
        InvoiceRequestDto invoiceRequest = new InvoiceRequestDto();
        invoiceRequest.setSaleId(saleId);
        invoiceRequest.setFecha(request.getFecha() != null ? request.getFecha() : LocalDate.now());
        invoiceRequest.setFolio(
                request.getFolio() != null && !request.getFolio().isBlank()
                        ? request.getFolio()
                        : "VENTA-" + saleId);
        invoiceRequest.setEstado("ACTIVA");
        invoiceRequest.setRazonSocialReceptor(request.getRazonSocialReceptor());
        invoiceRequest.setGiroReceptor(request.getGiroReceptor());
        invoiceRequest.setDireccionReceptor(request.getDireccionReceptor());
        invoiceRequest.setRutReceptor(request.getRutReceptor());
        invoiceRequest.setRazonSocialEmisor(request.getRazonSocialEmisor());
        invoiceRequest.setGiroEmisor(request.getGiroEmisor());
        invoiceRequest.setDireccionEmisor(request.getDireccionEmisor());
        invoiceRequest.setRutEmisor(request.getRutEmisor());
        return invoiceRequest;
    }

    private AccountingResponse armarAccountingResponse(SaleResponse sale, List<InvoiceResponseDto> invoices) {
        return AccountingResponse.builder()
                .sale(sale)
                .invoices(invoices)
                .build();
    }

    private AccountingCreateResponse armarAccountingCreateResponse(SaleResponse sale, InvoiceResponseDto invoice) {
        return AccountingCreateResponse.builder()
                .sale(sale)
                .invoice(invoice)
                .build();
    }

    private List<InvoiceResponseDto> filtrarFacturasPorVenta(List<InvoiceResponseDto> invoices, Long saleId) {
        return invoices.stream()
                .filter(invoice -> saleId.equals(invoice.getSaleId()))
                .toList();
    }
}
