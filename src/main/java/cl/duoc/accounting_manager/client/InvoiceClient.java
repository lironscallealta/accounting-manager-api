package cl.duoc.accounting_manager.client;

import java.util.List;

import org.springframework.stereotype.Component;

import org.springframework.web.reactive.function.client.WebClient;

import cl.duoc.accounting_manager.dto.request.invoice.InvoiceRequestDto;
import cl.duoc.accounting_manager.dto.response.invoice.InvoiceResponseDto;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InvoiceClient {

    private final WebClient webClientInvoice;

    public List<InvoiceResponseDto> getInvoices() {
        return webClientInvoice.get().uri("")
                .retrieve()
                .bodyToFlux(InvoiceResponseDto.class)
                .collectList()
                .block();
    }

    public InvoiceResponseDto getInvoiceByFolio(Long folio) {
        return webClientInvoice.get().uri("/{folio}", folio)
                .retrieve()
                .bodyToMono(InvoiceResponseDto.class)
                .block();
    }

    public InvoiceResponseDto createInvoice(InvoiceRequestDto request) {
        return webClientInvoice.post().uri("/", request)
                .retrieve()
                .bodyToMono(InvoiceResponseDto.class)
                .block();
    }

    public InvoiceResponseDto deleteInvoice(Long folio) {
        return webClientInvoice.post()
                .uri("/{folio}/anular", folio)
                .retrieve()
                .bodyToMono(InvoiceResponseDto.class)
                .block();
    }

}
