package com.nekrasov.contractapp.repository;

import com.nekrasov.contractapp.model.ContractPdf;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ContractPdfRepository extends JpaRepository<ContractPdf, Long> {
    List<ContractPdf> findByContractId(Long contractId);
}

