# Contract Management App

A Spring Boot application for managing contracts, generating and storing contract PDFs, and supporting multi-file upload/download per contract.

## Features
- Create, view, and list contracts
- Render contract details to PDF and store them in the database
- Upload and download multiple PDFs per contract
- Simple web UI for contract management
- REST API for integration


## Technologies Used

- Java 11+
- Spring Boot 2.7.x
- Spring MVC & Thymeleaf
- Gradle
- H2 Database (in-memory, for development)
- Lombok
- PDF generation (OpenPDF)
- HTML/CSS (for contract templates)

## How to Run Locally

1. **Clone the repository:**
   ```sh
   git clone <repo-url>
   cd contract-app
   ```
2. **Build the project:**
   ```sh
   ./gradlew build
   ```
3. **Run the application:**
   ```sh
   ./gradlew bootRun
   ```
4. **Access the app:**
   - Web UI: [http://localhost:8080/contract-form](http://localhost:8080/contract-form)
   - H2 Console: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)

## REST API Examples

### Create a Contract
```http
POST /contract
Content-Type: application/json
{
  "contractorName": "Test",
  "contractorDetails": "Details",
  "termsPricePayment": "Terms",
  "paymentRequisites": "Pay",
  "warranty": "Warranty",
  "partiesContacts": "Contacts",
  "contractNumberDate": "123"
}
```

### Download Latest PDF for a Contract
```http
GET /contract/{contractId}
```

### List All PDFs for a Contract
```http
GET /contract/{contractId}/pdfs
```

### Download a Specific PDF
```http
GET /contract/pdf/{pdfId}
```

### Upload a PDF for a Contract
```http
POST /contract/{contractId}/upload-pdf
Content-Type: multipart/form-data
file: <PDF file>
```

## TODO
- Add support for cloud provider deployment
- Add support for more templates
- Provide support for Word file generation

