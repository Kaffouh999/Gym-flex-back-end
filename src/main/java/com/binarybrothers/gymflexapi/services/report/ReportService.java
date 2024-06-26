package com.binarybrothers.gymflexapi.services.report;

import com.binarybrothers.gymflexapi.entities.Member;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ReportService {

    byte[] generateReport(List<Object> data, String nameFile, Map<String, Object> parameters, MediaType format);

    byte[] generateMemberCardReport(Member data) throws IOException;
}
