package com.nekrasov.contractapp.service;

import com.nekrasov.contractapp.dto.ContractDto;
import com.nekrasov.contractapp.model.Contract;
import com.nekrasov.contractapp.repository.ContractRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.thymeleaf.TemplateEngine;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ContractServiceTest {
    @Mock
    private ContractRepository contractRepository;
    @Mock
    private TemplateEngine templateEngine;
    @InjectMocks
    private ContractService contractService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave() {
        ContractDto dto = new ContractDto();
        dto.setContractorName("Test");
        dto.setContractorDetails("Details");
        dto.setTermsPricePayment("Terms");
        dto.setPaymentRequisites("Pay");
        dto.setWarranty("Warranty");
        dto.setPartiesContacts("Contacts");
        dto.setContractNumberDate("123");
        Contract contract = new Contract();
        contract.setId(1L);
        when(contractRepository.save(any(Contract.class))).thenReturn(contract);
        Contract saved = contractService.save(dto);
        assertNotNull(saved);
        assertEquals(1L, saved.getId());
    }
}