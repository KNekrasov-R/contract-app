package com.nekrasov.contractapp.controller;

import com.nekrasov.contractapp.dto.ContractDto;
import com.nekrasov.contractapp.model.Contract;
import com.nekrasov.contractapp.repository.ContractRepository;
import com.nekrasov.contractapp.service.ContractService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ContractControllerTest {
    private MockMvc mockMvc;

    @Mock
    private ContractService contractService;
    @Mock
    private ContractRepository contractRepository;
    @InjectMocks
    private ContractController contractController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(contractController).build();
    }

    @Test
    void testCreateContract() throws Exception {
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
        when(contractService.save(any(ContractDto.class))).thenReturn(contract);
        mockMvc.perform(post("/contract")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"contractorName\":\"Test\",\"contractorDetails\":\"Details\",\"termsPricePayment\":\"Terms\",\"paymentRequisites\":\"Pay\",\"warranty\":\"Warranty\",\"partiesContacts\":\"Contacts\",\"contractNumberDate\":\"123\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    void testGetContractPdf_NotFound() throws Exception {
        when(contractRepository.findById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/contract/1"))
                .andExpect(status().isNotFound());
    }
} 