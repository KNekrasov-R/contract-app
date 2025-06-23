package com.nekrasov.contractapp.model;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "contract_pdfs")
public class ContractPdf {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id", nullable = false)
    private Contract contract;

    @Column(name = "file_name")
    private String fileName;

    @Lob
    @Column(name = "data", nullable = false)
    private byte[] data;

    @Column(name = "upload_date")
    private LocalDateTime uploadDate = LocalDateTime.now();
}

