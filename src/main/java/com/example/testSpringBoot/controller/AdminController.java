package com.example.testSpringBoot.controller;

import com.example.testSpringBoot.service.ExcelExportService;
import com.example.testSpringBoot.service.LoginLogService;
import com.example.testSpringBoot.service.VocabularyService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private VocabularyService vocabularyService;

    @Autowired
    private LoginLogService loginLogService;

    @Autowired
    private ExcelExportService excelExportService; // TIÊM SERVICE EXCEL VÀO

    @GetMapping("/vocabulary")
    public String showManageVocabularyPage(Model model) {
        model.addAttribute("vocabularies", vocabularyService.findAll());
        return "manage-vocabulary";
    }

    @GetMapping("/login-logs")
    public String showLoginLogsPage(Model model) {
        model.addAttribute("loginLogs", loginLogService.findAll());
        return "login-logs";
    }

    @PostMapping("/vocabulary/import")
    public String handleVocabularyImport(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng chọn một file XML để tải lên.");
            return "redirect:/admin/vocabulary";
        }

        try {
            vocabularyService.importFromXml(file);
            redirectAttributes.addFlashAttribute("message", "Import từ vựng từ file XML thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi import file: " + e.getMessage());
            e.printStackTrace();
        }

        return "redirect:/admin/vocabulary";
    }

    @GetMapping("/vocabulary/export")
    public void exportVocabularyToXml(HttpServletResponse response) throws IOException {
        try {
            String xmlContent = vocabularyService.exportToXml();
            response.setContentType("application/xml;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=\"vocabulary_export.xml\"");
            response.getOutputStream().write(xmlContent.getBytes(StandardCharsets.UTF_8));
            response.flushBuffer();
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi khi tạo file XML: " + e.getMessage());
        }
    }

    @GetMapping("/courses/{courseId}/export-members")
    public void exportCourseMembers(@PathVariable Integer courseId, HttpServletResponse response) throws IOException {
        String filename = "members_course_" + courseId + ".xlsx";
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");

        ByteArrayInputStream stream = excelExportService.exportMembersToExcel(courseId);

        // Sử dụng hàm có sẵn của Java, không cần thêm thư viện ngoài
        stream.transferTo(response.getOutputStream());

        response.flushBuffer();
    }
}