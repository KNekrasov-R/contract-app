package com.nekrasov.contractapp.controller;


import com.nekrasov.contractapp.service.ContractService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class ContractUiControllerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        ContractService contractService = Mockito.mock(ContractService.class);
        ContractUiController controller = new ContractUiController(contractService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setViewResolvers((viewName, locale) -> new View() {
                    @Override
                    public String getContentType() {
                        return "text/html";
                    }

                    @Override
                    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) {
                    }
                })
                .build();
    }

    @Test
    void testShowContractForm() throws Exception {
        mockMvc.perform(get("/contract-form"))
                .andExpect(status().isOk())
                .andExpect(view().name("contract-form"));
    }
}