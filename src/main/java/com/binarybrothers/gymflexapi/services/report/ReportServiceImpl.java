package com.binarybrothers.gymflexapi.services.report;

import com.binarybrothers.gymflexapi.entities.Member;
import com.binarybrothers.gymflexapi.entities.SubscriptionMember;
import com.binarybrothers.gymflexapi.services.member.MemberService;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.binarybrothers.gymflexapi.utils.QRCodeGenerator.generateQRCodeBase64;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    @Value("${NGINX_URL}")
    private String ngnixUrl;

    private final MemberService memberService;

    public byte[] generateReport(List<Object> data, String nameFile, Map<String, Object> parameters, MediaType format) {
        JasperPrint jasperPrint;
        byte[] reportContent;
        try {

            File file = ResourceUtils.getFile("classpath:reports/" + nameFile);
            JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

            //Set report data
            JRDataSource dataSource;
            if (data == null || data.isEmpty()) {
                // Use an empty data source instead of null
                dataSource = new JREmptyDataSource();
            } else {
                dataSource = new JRBeanCollectionDataSource(data);
            }

            //Fill report
            jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            if (format == MediaType.APPLICATION_PDF) {
                reportContent = JasperExportManager.exportReportToPdf(jasperPrint);
            } else if (format == MediaType.APPLICATION_XML) {
                reportContent = JasperExportManager.exportReportToXml(jasperPrint).getBytes();
            } else {
                throw new IllegalArgumentException("Unknown report format");
            }
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex);
        }
        return reportContent;
    }

    public byte[] generateMemberCardReport(Member member) throws IOException {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("MEMBER_GYM_NAME", member.getGymBranch().getName());
        parameters.put("MEMBER_NAME", member.getOnlineUser().getFirstName() + " " + member.getOnlineUser().getLastName());
        parameters.put("MEMBER_CIN", member.getCin());
        parameters.put("MEMBER_NBR", member.getId().toString());
        parameters.put("MEMBER_PROFILE_PICTURE", member.getOnlineUser().getProfilePicture());
        parameters.put("NGINX_URL", ngnixUrl);
        Optional<SubscriptionMember> maxIdSubscriptionMember = member.getSubscriptionMemberList().stream()
                .max(Comparator.comparing(SubscriptionMember::getId));

        if(maxIdSubscriptionMember.isPresent()) {
            SubscriptionMember lastSubscriptionMember = maxIdSubscriptionMember.get();
            parameters.put("QR_CODE", generateQRCodeBase64(String.valueOf(lastSubscriptionMember.getCodeSubscription())));
        }
        return generateReport(null, "gymflexMemberCard.jrxml", parameters, MediaType.APPLICATION_PDF);
    }


}
