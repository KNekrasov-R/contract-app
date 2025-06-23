package com.nekrasov.contractapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ContractListDto {
    private Long id;
    private String contractorName;
    private LocalDateTime createdAt;
}

