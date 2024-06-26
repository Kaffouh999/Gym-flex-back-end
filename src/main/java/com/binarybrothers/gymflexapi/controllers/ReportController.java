package com.binarybrothers.gymflexapi.controllers;

import com.binarybrothers.gymflexapi.services.member.MemberService;
import com.binarybrothers.gymflexapi.services.report.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ReportController {

    private final ReportService reportService;

    private final MemberService memberService;

    @GetMapping("/{reportName}")
    public ResponseEntity<byte[]> getReport(@PathVariable String reportName, @RequestParam(required = false) Map<String, Object> parameters) {
        try {
            byte[] reportBytes = reportService.generateMemberCardReport(memberService.findById(1L).get());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            return new ResponseEntity<>(reportBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
