package cl.duoc.accounting_manager.service;

import org.springframework.stereotype.Service;

import cl.duoc.accounting_manager.client.InvoiceClient;
import cl.duoc.accounting_manager.client.SalesClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountingService {

    private final SalesClient salesClient;
    private final InvoiceClient invoiceClient;

    

    

}
