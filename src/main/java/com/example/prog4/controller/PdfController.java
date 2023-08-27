package com.example.prog4.controller;

import com.example.prog4.service.EmployeeService;
import com.example.prog4.service.PdfGenerationService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PdfController {

    private final PdfGenerationService pdfGenerationService;
    private final EmployeeService employeeService;

    public PdfController(PdfGenerationService pdfGenerationService, EmployeeService employeeService) {
        this.pdfGenerationService = pdfGenerationService;
        this.employeeService = employeeService;
    }

    @GetMapping("/generate-pdf/{employeeId}")
    public ResponseEntity<byte[]> generatePdf(@PathVariable String employeeId) {
        com.example.prog4.repository.entity.Employee employee = employeeService.getOne(employeeId);

        if (employee == null) {
            return ResponseEntity.notFound().build();
        }

        byte[] pdfBytes = pdfGenerationService.generatePdf(employee);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "employee-details.pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }
}
