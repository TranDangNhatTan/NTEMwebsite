package com.example.testSpringBoot.controller;

import com.example.testSpringBoot.service.LoginLogService; // Thêm import
import com.example.testSpringBoot.service.VocabularyService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private VocabularyService vocabularyService;

    @Autowired
    private LoginLogService loginLogService; // Thêm service mới

    @GetMapping("/vocabulary")
    public String showManageVocabularyPage(Model model) {
        model.addAttribute("vocabularies", vocabularyService.findAll());
        return "manage-vocabulary";
    }

    // THÊM PHƯƠNG THỨC MỚI
    @GetMapping("/login-logs")
    public String showLoginLogsPage(Model model) {
        model.addAttribute("loginLogs", loginLogService.findAll());
        return "login-logs"; // Tên của file view HTML mới
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
}