package com.example.GymInTheBack.services.report;

import com.example.GymInTheBack.entities.OnlineUser;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    public byte[] generateReport(List<Object> data, String nameFile, Map<String, Object> parameters, MediaType format) {
        JasperPrint jasperPrint = null;
        byte[] reportContent;
        try {

            File file = ResourceUtils.getFile("classpath:reports/" + nameFile);
            JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

            //Set report data
            data = new ArrayList<>();
            data.add(new OnlineUser(1L,"othman","kaffouh","othman","othman@gmail.com","dd","ee",null,null,null));
            data.add(new OnlineUser(1L,"ahmed","ghandour","othman","othman@gmail.com","dd","ee",null,null,null));
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(data);

            //Add QR code to parameters
            parameters.put("QR_CODE", generateQRCodeBase64());
            //Fill report
            jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            if (format == MediaType.APPLICATION_PDF) {
                reportContent = JasperExportManager.exportReportToPdf(jasperPrint);
            } else if (format == MediaType.APPLICATION_XML) {
                reportContent = JasperExportManager.exportReportToXml(jasperPrint).getBytes();
            } else {
                throw new RuntimeException("Unknown report format");
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return reportContent;
    }

    private static String generateQRCodeBase64()
            throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode("AD324805", BarcodeFormat.QR_CODE,0, 0);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        byte[] pngData = pngOutputStream.toByteArray();

        return Base64.getEncoder().encodeToString(pngData);
    }
}
