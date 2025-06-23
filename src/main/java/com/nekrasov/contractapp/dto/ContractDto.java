package com.nekrasov.contractapp.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class ContractDto {
    @NotBlank
    @Size(max = 512)
    private String contractorName;

    @NotBlank
    private String contractorDetails;

    @NotBlank
    private String termsPricePayment;

    @NotBlank
    private String paymentRequisites;

    @NotBlank
    private String warranty;

    @NotBlank
    private String partiesContacts;

    @NotBlank
    @Size(max = 128)
    private String contractNumberDate;
} 