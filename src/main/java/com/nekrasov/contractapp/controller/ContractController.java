package com.nekrasov.contractapp.controller;

import com.nekrasov.contractapp.dto.ContractDto;
import com.nekrasov.contractapp.model.Contract;
import com.nekrasov.contractapp.model.ContractPdf;
import com.nekrasov.contractapp.repository.ContractPdfRepository;
import com.nekrasov.contractapp.repository.ContractRepository;
import com.nekrasov.contractapp.service.ContractService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ContractController {
    private final ContractService contractService;
    private final ContractRepository contractRepository;
    private final ContractPdfRepository contractPdfRepository;

    @PostMapping("/contract")
    public ResponseEntity<Void> createContract(@Valid @RequestBody ContractDto dto) {
        Contract contract = contractService.save(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(contract.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/contract/{id}")
    public ResponseEntity<byte[]> getContractPdf(@PathVariable Long id) {
        Optional<Contract> contractOpt = contractRepository.findById(id);
        if (contractOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Contract contract = contractOpt.get();
        contract.setDownloadCount(contract.getDownloadCount() == null ? 1 : contract.getDownloadCount() + 1);
        contractRepository.save(contract);
        // Get the latest PDF for this contract (if any)
        List<ContractPdf> pdfs = contractPdfRepository.findByContractId(id);
        if (pdfs.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        ContractPdf latestPdf = pdfs.stream()
            .max(java.util.Comparator.comparing(ContractPdf::getUploadDate))
            .orElse(null);
        if (latestPdf == null) {
            return ResponseEntity.notFound().build();
        }
        String filename = URLEncoder.encode(latestPdf.getFileName(), StandardCharsets.UTF_8);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + filename)
                .contentType(MediaType.APPLICATION_PDF)
                .body(latestPdf.getData());
    }

    @PostMapping("/contract/save")
    public ResponseEntity<Void> saveContract(@Valid @RequestBody ContractDto dto) {
        contractService.save(dto);
        return ResponseEntity.status(HttpStatus.FOUND).header(HttpHeaders.LOCATION, "/contracts").build();
    }

    @PostMapping("/contract/render")
    public ResponseEntity<byte[]> renderAndDownloadContract(@Valid @RequestBody ContractDto dto) {
        Contract contract = contractService.save(dto);
        // Get the latest PDF for this contract (just created)
        List<ContractPdf> pdfs = contractPdfRepository.findByContractId(contract.getId());
        if (pdfs.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        ContractPdf latestPdf = pdfs.stream()
            .max(java.util.Comparator.comparing(ContractPdf::getUploadDate))
            .orElse(null);
        if (latestPdf == null) {
            return ResponseEntity.notFound().build();
        }
        String filename = URLEncoder.encode(latestPdf.getFileName(), StandardCharsets.UTF_8);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + filename);
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.add("Location", "/contracts");
        return new ResponseEntity<>(latestPdf.getData(), headers, HttpStatus.OK);
    }

    @GetMapping("/contract/{contractId}/pdfs")
    public List<ContractPdf> getContractPdfs(@PathVariable Long contractId) {
        return contractPdfRepository.findByContractId(contractId);
    }

    @GetMapping("/contract/pdf/{pdfId}")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable Long pdfId) {
        ContractPdf pdf = contractPdfRepository.findById(pdfId).orElse(null);
        if (pdf == null) {
            return ResponseEntity.notFound().build();
        }
        String filename = URLEncoder.encode(pdf.getFileName(), StandardCharsets.UTF_8);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + filename)
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf.getData());
    }

    @PostMapping("/contract/{id}/upload-pdf")
    public ResponseEntity<?> uploadPdf(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty() || !file.getContentType().equals("application/pdf")) {
            return ResponseEntity.badRequest().body("Only PDF files are allowed");
        }
        Optional<Contract> contractOpt = contractRepository.findById(id);
        if (contractOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Contract contract = contractOpt.get();
        ContractPdf pdf = new ContractPdf();
        pdf.setContract(contract);
        pdf.setFileName(file.getOriginalFilename());
        pdf.setData(file.getBytes());
        pdf.setUploadDate(java.time.LocalDateTime.now());
        contractPdfRepository.save(pdf);
        return ResponseEntity.status(HttpStatus.FOUND).header(HttpHeaders.LOCATION, "/contracts").build();
    }
}
