package com.nekrasov.contractapp.model;

import javax.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "contracts")
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "contractor_name", length = 512, nullable = false)
    private String contractorName;

    @Column(name = "contractor_details", nullable = false)
    private String contractorDetails;

    @Column(name = "terms_price_payment", nullable = false)
    private String termsPricePayment;

    @Column(name = "payment_requisites", nullable = false)
    private String paymentRequisites;

    @Column(name = "warranty", nullable = false)
    private String warranty;

    @Column(name = "parties_contacts", nullable = false)
    private String partiesContacts;

    @Column(name = "contract_number_date", length = 128, nullable = false)
    private String contractNumberDate;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "download_count")
    private Integer downloadCount = 0;

    @Column(name = "contractor_representative")
    private String contractorRepresentative;

    @Column(name = "contractor_representative_basis")
    private String contractorRepresentativeBasis;

    @Column(name = "start_date")
    private String startDate;

    @Column(name = "end_date")
    private String endDate;

    @Column(name = "price")
    private String price;

    @Column(name = "price_words")
    private String priceWords;

    @Column(name = "advance_percent")
    private Integer advancePercent;

    @Column(name = "advance_days")
    private Integer advanceDays;

    @Column(name = "rest_days")
    private Integer restDays;

    @Column(name = "customer_bank_details")
    private String customerBankDetails;

    @Column(name = "contractor_address")
    private String contractorAddress;

    @Column(name = "contractor_inn_kpp")
    private String contractorInnKpp;

    @Column(name = "contractor_ogrn")
    private String contractorOgrn;

    @Column(name = "contractor_bank")
    private String contractorBank;

    @Column(name = "contractor_rs")
    private String contractorRs;

    @Column(name = "contractor_ks")
    private String contractorKs;

    @Column(name = "contractor_bik")
    private String contractorBik;

    @Column(name = "contractor_representative_short")
    private String contractorRepresentativeShort;

    @OneToMany(mappedBy = "contract", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<ContractPdf> pdfs = new java.util.ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
