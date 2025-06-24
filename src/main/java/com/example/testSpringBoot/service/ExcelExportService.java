package com.example.testSpringBoot.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public interface ExcelExportService {
    ByteArrayInputStream exportMembersToExcel(Integer courseId) throws IOException;
}