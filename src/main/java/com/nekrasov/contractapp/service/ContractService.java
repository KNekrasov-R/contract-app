package com.nekrasov.contractapp.service;

import com.nekrasov.contractapp.dto.ContractDto;
import com.nekrasov.contractapp.dto.ContractListDto;
import com.nekrasov.contractapp.model.Contract;
import com.nekrasov.contractapp.model.ContractPdf;
import com.nekrasov.contractapp.repository.ContractPdfRepository;
import com.nekrasov.contractapp.repository.ContractRepository;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContractService {
    private final ContractRepository contractRepository;
    private final ContractPdfRepository contractPdfRepository;
    private final TemplateEngine templateEngine;

    @Transactional
    public Contract save(ContractDto dto) {
        Contract contract = new Contract();
        contract.setContractorName(dto.getContractorName());
        contract.setContractorDetails(dto.getContractorDetails());
        contract.setTermsPricePayment(dto.getTermsPricePayment());
        contract.setPaymentRequisites(dto.getPaymentRequisites());
        contract.setWarranty(dto.getWarranty());
        contract.setPartiesContacts(dto.getPartiesContacts());
        contract.setContractNumberDate(dto.getContractNumberDate());
        contract = contractRepository.save(contract);
        // Render and store PDF as a new ContractPdf entity
        ContractPdf pdf = renderPdf(contract);
        contractPdfRepository.save(pdf);
        contract.getPdfs().add(pdf);
        return contract;
    }

    public ContractPdf renderPdf(Contract contract) {
        final String fontPath = "/fonts/DejaVuSans.ttf";
        Context ctx = new Context(Locale.forLanguageTag("ru"));
        ctx.setVariable("contract", contract);
        String html = templateEngine.process("contract-template.html", ctx);
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(html, "");
            // Register DejaVu Sans font for Cyrillic support
            try (InputStream fontStream = getClass().getResourceAsStream(fontPath)) {
                if (fontStream != null) {
                    builder.useFont(() -> getClass().getResourceAsStream(fontPath), "DejaVu Sans",
                            400, PdfRendererBuilder.FontStyle.NORMAL, true);
                }
            }
            builder.toStream(out);
            builder.run();
            ContractPdf pdf = new ContractPdf();
            pdf.setContract(contract);
            pdf.setFileName("contract-" + (contract.getId() != null ? contract.getId() : "new") + "-" + System.currentTimeMillis() + ".pdf");
            pdf.setData(out.toByteArray());
            pdf.setUploadDate(LocalDateTime.now());
            return pdf;
        } catch (Exception e) {
            throw new RuntimeException("PDF generation failed", e);
        }
    }

    public byte[] getPdfById(Long pdfId) {
        return contractPdfRepository.findById(pdfId)
                .map(ContractPdf::getData)
                .orElse(null);
    }

    public List<ContractListDto> getAllContracts() {
        return contractRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(c -> new ContractListDto(c.getId(), c.getContractorName(), c.getCreatedAt()))
                .collect(Collectors.toList());
    }
}
