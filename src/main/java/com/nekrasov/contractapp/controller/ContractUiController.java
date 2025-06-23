package com.nekrasov.contractapp.controller;

import com.nekrasov.contractapp.dto.ContractDto;
import com.nekrasov.contractapp.service.ContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping
public class ContractUiController {
    private final ContractService contractService;

    @GetMapping("/contract-form")
    public String showContractForm(Model model) {
        model.addAttribute("contractDto", new ContractDto());
        return "contract-form";
    }

    @GetMapping("/contracts")
    public String showContracts(Model model) {
        model.addAttribute("contracts", contractService.getAllContracts());
        return "contract-list";
    }
}
