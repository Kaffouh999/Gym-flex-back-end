package com.binarybrothers.gymflexapi.services.report;

import org.springframework.http.MediaType;

import java.util.List;
import java.util.Map;

public interface ReportService {

    byte[] generateReport(List<Object> data, String nameFile, Map<String, Object> parameters, MediaType format);
}
