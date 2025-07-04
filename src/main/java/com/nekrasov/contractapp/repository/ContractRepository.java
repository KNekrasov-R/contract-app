package com.nekrasov.contractapp.repository;

import com.nekrasov.contractapp.model.Contract;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContractRepository extends JpaRepository<Contract, Long> {
    List<Contract> findAllByOrderByCreatedAtDesc();
}
