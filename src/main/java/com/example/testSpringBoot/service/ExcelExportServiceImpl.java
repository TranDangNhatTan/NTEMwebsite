package com.example.testSpringBoot.service;

import com.example.testSpringBoot.model.Enrollment;
import com.example.testSpringBoot.repository.EnrollmentRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelExportServiceImpl implements ExcelExportService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Override
    public ByteArrayInputStream exportMembersToExcel(Integer courseId) throws IOException {
        String[] columns = {"Student ID", "Full Name", "Email", "Enrollment Status"};

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Course Members");

            // Tạo hàng Header
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }

            // Lấy dữ liệu và điền vào các hàng
            List<Enrollment> enrollments = enrollmentRepository.findByCourseIdWithUserDetails(courseId);
            int rowIdx = 1;
            for (Enrollment enrollment : enrollments) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(enrollment.getUser().getUserId());
                row.createCell(1).setCellValue(enrollment.getUser().getFullName());
                row.createCell(2).setCellValue(enrollment.getUser().getEmail());
                row.createCell(3).setCellValue(enrollment.getStatus());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }
}