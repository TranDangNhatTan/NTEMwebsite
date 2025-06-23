package com.example.testSpringBoot.controller;

import com.example.testSpringBoot.model.*;
import com.example.testSpringBoot.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private CourseService courseService;
    @Autowired
    private LessonService lessonService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private EnrollmentService enrollmentService;
    @Autowired
    private UserService userService;
    @Autowired
    private CourseRegistrationService courseRegistrationService;

    private static final String UPLOAD_DIR = "Uploads/lessons/";
    private static final long MAX_FILE_SIZE = 100 * 1024 * 1024; // 100MB

    @GetMapping({"/", "/index"})
    public String showHomePage(Model model, HttpServletRequest request) {
        model.addAttribute("currentPage", request.getRequestURI());
        addUserFullNameToModel(model);
        return "index";
    }

    @GetMapping("/events")
    public String showEventsPage(Model model, HttpServletRequest request) {
        model.addAttribute("currentPage", request.getRequestURI());
        addUserFullNameToModel(model);
        return "events";
    }

    @GetMapping("/achievements")
    public String showAchievementsPage(Model model, HttpServletRequest request) {
        model.addAttribute("currentPage", request.getRequestURI());
        addUserFullNameToModel(model);
        return "achievements";
    }

    @GetMapping("/courses")
    public String showCoursesPage(Model model, HttpServletRequest request) {
        model.addAttribute("currentPage", request.getRequestURI());
        addUserFullNameToModel(model);
        model.addAttribute("courses", courseService.findAll());
        return "courses";
    }

    @GetMapping("/about")
    public String showAboutPage(Model model, HttpServletRequest request) {
        model.addAttribute("currentPage", request.getRequestURI());
        addUserFullNameToModel(model);
        return "about";
    }

    @GetMapping("/my-courses")
    public String showMyCoursesPage(Model model, HttpServletRequest request) {
        model.addAttribute("currentPage", request.getRequestURI());
        addUserFullNameToModel(model);
        return "my-courses";
    }

    @GetMapping("/change-password")
    public String showChangePasswordPage(Model model, HttpServletRequest request) {
        model.addAttribute("currentPage", request.getRequestURI());
        addUserFullNameToModel(model);
        return "change-password";
    }

    @GetMapping("/admin")
    public String showAdminPage(Model model, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof User) {
            User user = (User) auth.getPrincipal();
            logger.debug("Admin check for user: {}, role: {}, authorities: {}", user.getUsername(),
                    user.getRole() != null ? user.getRole().getRoleType() : "null", auth.getAuthorities());
            if (user.getRole() == null || user.getRole().getRoleType() != Role.RoleType.ADMIN) {
                logger.debug("User {} is not authorized as ADMIN, redirecting to /", user.getUsername());
                return "redirect:/";
            }
            model.addAttribute("lastLogin", user.getCreatedAt() != null ? user.getCreatedAt().toString() : "N/A");
        } else {
            logger.debug("No authenticated user, redirecting to /");
            return "redirect:/";
        }
        model.addAttribute("currentPage", request.getRequestURI());
        addUserFullNameToModel(model);
        return "admin";
    }

    @GetMapping("/admin/managecourse")
    public String showManageCourseManagement(Model model, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof User) {
            User user = (User) auth.getPrincipal();
            if (user.getRole() == null || user.getRole().getRoleType() != Role.RoleType.ADMIN) {
                return "redirect:/";
            }
        }
        model.addAttribute("currentPage", request.getRequestURI());
        model.addAttribute("courses", courseService.findAll());
        addUserFullNameToModel(model);
        return "managecourse";
    }

    @GetMapping("/admin/managelessons")
    public String showManageLessonsManagement(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof User) {
            User user = (User) auth.getPrincipal();
            if (user.getRole() == null || user.getRole().getRoleType() != Role.RoleType.ADMIN) {
                return "redirect:/";
            }
        }
        try {
            model.addAttribute("currentPage", request.getRequestURI());
            model.addAttribute("lessons", lessonService.findAllWithCourse());
            model.addAttribute("courses", courseService.findAll());
            model.addAttribute("lesson", new Lesson());
            addUserFullNameToModel(model);
            return "managelessons";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi tải trang quản lý bài học: " + e.getMessage());
            return "redirect:/error";
        }
    }

    @GetMapping("/admin/manageenrollments")
    public String showManageEnrollmentsManagement(Model model, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof User) {
            User user = (User) auth.getPrincipal();
            if (user.getRole() == null || user.getRole().getRoleType() != Role.RoleType.ADMIN) {
                return "redirect:/";
            }
        }
        model.addAttribute("currentPage", request.getRequestURI());
        model.addAttribute("enrollments", enrollmentService.findAll());
        addUserFullNameToModel(model);
        return "manageenrollments";
    }

    @GetMapping("/admin/approve-manageenrollments")
    public String approveManageEnrollments(@RequestParam("enrollmentId") Long enrollmentId, Model model) {
        enrollmentService.approve(enrollmentId);
        model.addAttribute("message", "Phê duyệt đăng ký thành công!");
        return "redirect:/admin/manageenrollments";
    }

    @GetMapping("/admin/manageresources")
    public String showManageResourcesManagement(Model model, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof User) {
            User user = (User) auth.getPrincipal();
            if (user.getRole() == null || user.getRole().getRoleType() != Role.RoleType.ADMIN) {
                return "redirect:/";
            }
        }
        model.addAttribute("currentPage", request.getRequestURI());
        model.addAttribute("resources", resourceService.findAll());
        model.addAttribute("lessons", lessonService.findAll());
        addUserFullNameToModel(model);
        return "manageresources";
    }

    @PostMapping("/register-course")
    public String registerCourse(@RequestParam("courseId") Integer courseId, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof User) {
            User user = (User) auth.getPrincipal();
            if (user.getRole() == null || (user.getRole().getRoleType() != Role.RoleType.ADMIN && user.getRole().getRoleType() != Role.RoleType.STUDENT)) {
                model.addAttribute("error", "Chỉ admin và học sinh mới có thể đăng ký khóa học!");
                return "redirect:/courses";
            }
            try {
                courseRegistrationService.registerForCourse(courseId);
                model.addAttribute("message", "Đăng ký khóa học thành công!");
            } catch (Exception e) {
                model.addAttribute("error", "Không thể đăng ký khóa học: " + e.getMessage());
            }
        } else {
            model.addAttribute("error", "Vui lòng đăng nhập!");
        }
        return "redirect:/courses";
    }

    @PostMapping("/admin/add-managecourse")
    public String addManageCourse(@RequestParam("title") String title,
                                  @RequestParam("description") String description,
                                  @RequestParam("status") String status,
                                  @RequestParam("teacherId") Integer teacherId,
                                  Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof User) {
            User user = (User) auth.getPrincipal();
            if (user.getRole() == null || user.getRole().getRoleType() != Role.RoleType.ADMIN) {
                return "redirect:/";
            }
        }
        Course course = new Course();
        course.setTitle(title);
        course.setDescription(description);
        course.setStatus(status);
        course.setTeacherId(teacherId);
        course.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        courseService.save(course);
        model.addAttribute("message", "Thêm khóa học thành công!");
        return "redirect:/admin/managecourse";
    }

    @PostMapping("/admin/add-managelessons")
    public String addManageLessons(@RequestParam(value = "courseId", required = false) Integer courseId,
                                   @RequestParam("title") String title,
                                   @RequestParam("content") String content,
                                   @RequestParam(value = "video", required = false) MultipartFile video,
                                   @RequestParam(value = "material", required = false) MultipartFile material,
                                   RedirectAttributes redirectAttributes) throws IOException {
        if (courseId == null || title == null || title.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng chọn khóa học và nhập tiêu đề bài học!");
            return "redirect:/admin/managelessons";
        }
        Optional<Course> courseOptional = courseService.findById(courseId);
        if (courseOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Khóa học không tồn tại! Vui lòng chọn khóa học hợp lệ.");
            return "redirect:/admin/managelessons";
        }

        Lesson lesson = new Lesson();
        lesson.setCourseId(courseId);
        lesson.setTitle(title);
        lesson.setContent(content);
        lesson.setLessonOrder(lessonService.findMaxOrderByCourseId(courseId).orElse(0) + 1);

        if (video != null && !video.isEmpty()) {
            if (video.getSize() > MAX_FILE_SIZE) {
                redirectAttributes.addFlashAttribute("error", "Video vượt quá kích thước tối đa (100MB)!");
                return "redirect:/admin/managelessons";
            }
            String contentType = video.getContentType();
            if (contentType == null || !contentType.startsWith("video/")) {
                redirectAttributes.addFlashAttribute("error", "Tệp video không hợp lệ! Chỉ hỗ trợ MP4, AVI.");
                return "redirect:/admin/managelessons";
            }
            String fileName = System.nanoTime() + "_" + video.getOriginalFilename();
            Path videoPath = Paths.get(UPLOAD_DIR + fileName);
            Files.createDirectories(videoPath.getParent());
            Files.copy(video.getInputStream(), videoPath);
            lesson.setVideoUrl(fileName);
        }

        if (material != null && !material.isEmpty()) {
            if (material.getSize() > MAX_FILE_SIZE) {
                redirectAttributes.addFlashAttribute("error", "Tài liệu vượt quá kích thước tối đa (100MB)!");
                return "redirect:/admin/managelessons";
            }
            String materialType = material.getContentType();
            if (materialType == null || !materialType.equals("application/pdf")) {
                redirectAttributes.addFlashAttribute("error", "Tệp tài liệu không hợp lệ! Chỉ hỗ trợ PDF.");
                return "redirect:/admin/managelessons";
            }
            String fileName = System.nanoTime() + "_" + material.getOriginalFilename();
            Path materialPath = Paths.get(UPLOAD_DIR + fileName);
            Files.createDirectories(materialPath.getParent());
            Files.copy(material.getInputStream(), materialPath);
            lesson.setMaterialUrl(fileName);
        } else {
            redirectAttributes.addFlashAttribute("error", "Vui lòng tải lên tài liệu PDF!");
            return "redirect:/admin/managelessons";
        }

        try {
            lessonService.save(lesson);
            redirectAttributes.addFlashAttribute("message", "Thêm bài học thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi thêm bài học: " + e.getMessage());
            logger.error("Lỗi khi lưu bài học: ", e);
        }
        return "redirect:/admin/managelessons";
    }

    @PostMapping("/admin/add-manageresources")
    public String addManageResources(@RequestParam("lessonId") Integer lessonId,
                                     @RequestParam("file") MultipartFile file,
                                     Model model) throws IOException {
        if (!file.isEmpty()) {
            String fileName = System.nanoTime() + "_" + file.getOriginalFilename();
            Path path = Paths.get("uploads/resources/" + fileName);
            Files.createDirectories(path.getParent());
            Files.copy(file.getInputStream(), path);
            Resource resource = new Resource();
            resource.setLessonId(lessonId);
            resource.setFileName(file.getOriginalFilename());
            resource.setFilePath(fileName);
            String contentType = file.getContentType();
            if (contentType != null) {
                if (contentType.equals("application/pdf")) {
                    resource.setFileType(Resource.FileType.pdf);
                } else if (contentType.startsWith("video/")) {
                    resource.setFileType(Resource.FileType.video);
                } else if (contentType.startsWith("audio/")) {
                    resource.setFileType(Resource.FileType.audio);
                } else {
                    model.addAttribute("error", "Loại tệp không hỗ trợ!");
                    return "redirect:/admin/manageresources";
                }
            }
            resource.setUploadedBy(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId());
            resource.setUploadedAt(new Timestamp(System.currentTimeMillis()));
            resourceService.save(resource);
            model.addAttribute("message", "Tải lên tài nguyên thành công!");
        } else {
            model.addAttribute("error", "Vui lòng chọn tệp để tải lên!");
        }
        return "redirect:/admin/manageresources";
    }

    @GetMapping("/admin/download-manageresources/{id}")
    public void downloadManageResources(@PathVariable Long id, HttpServletResponse response) throws IOException {
        Optional<Resource> resourceOptional = resourceService.findById(id);
        if (resourceOptional.isPresent()) {
            Resource resource = resourceOptional.get();
            Path filePath = Paths.get("uploads/resources/" + resource.getFilePath());
            String contentType = switch (resource.getFileType()) {
                case pdf -> "application/pdf";
                case video -> "video/mp4";
                case audio -> "audio/mpeg";
            };
            response.setContentType(contentType);
            response.setHeader("Content-Disposition", "attachment; filename=" + resource.getFileName());
            Files.copy(filePath, response.getOutputStream());
            response.getOutputStream().flush();
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Tài nguyên không được tìm thấy!");
        }
    }

    @GetMapping("/admin/reject-manageenrollments")
    public String rejectManageEnrollments(@RequestParam("enrollmentId") Long enrollmentId, Model model) {
        enrollmentService.reject(enrollmentId);
        model.addAttribute("message", "Từ chối đăng ký thành công!");
        return "redirect:/admin/manageenrollments";
    }

    @GetMapping("/admin/delete-manageenrollments")
    public String deleteManageEnrollments(@RequestParam("enrollmentId") Long enrollmentId, Model model) {
        enrollmentService.deleteById(enrollmentId);
        model.addAttribute("message", "Xóa đăng ký thành công!");
        return "redirect:/admin/manageenrollments";
    }

    @GetMapping("/admin/delete-managecourse")
    public String deleteManageCourse(@RequestParam("courseId") Integer courseId, Model model) {
        courseService.deleteById(courseId);
        model.addAttribute("message", "Xóa khóa học thành công!");
        return "redirect:/admin/managecourse";
    }

    @GetMapping("/admin/delete-managelessons")
    public String deleteManageLessons(@RequestParam("lessonId") Integer lessonId, Model model) {
        lessonService.deleteById(lessonId);
        model.addAttribute("message", "Xóa bài học thành công!");
        return "redirect:/admin/managelessons";
    }

    @GetMapping("/resources/material/{id}")
    public void downloadMaterial(@PathVariable Integer id, HttpServletResponse response) throws IOException {
        Optional<Lesson> lessonOptional = lessonService.findById(id);
        if (lessonOptional.isPresent() && lessonOptional.get().getMaterialUrl() != null) {
            Path filePath = Paths.get(UPLOAD_DIR + lessonOptional.get().getMaterialUrl());
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=" + filePath.getFileName());
            Files.copy(filePath, response.getOutputStream());
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Bài học hoặc tài liệu không được tìm thấy!");
        }
    }

    // Thêm endpoint để lấy danh sách khóa học đã đăng ký
    @GetMapping("/api/my-courses")
    @ResponseBody
    public List<Course> getMyCourses() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof User) {
            User user = (User) auth.getPrincipal();
            return enrollmentService.findCoursesByUserId(user.getUserId());
        }
        return List.of();
    }

    // Thêm endpoint để lấy danh sách bài học theo khóa học
    @GetMapping("/api/lessons/{courseId}")
    @ResponseBody
    public List<Lesson> getLessonsByCourseId(@PathVariable Integer courseId) {
        return lessonService.findByCourseId(courseId);
    }

    private void addUserFullNameToModel(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();
            model.addAttribute("fullName", user.getFullName());
            boolean isAdmin = user.getRole() != null && user.getRole().getRoleType() == Role.RoleType.ADMIN;
            model.addAttribute("isAdmin", isAdmin);
            logger.debug("User: {}, isAdmin: {}", user.getUsername(), isAdmin);
        } else {
            model.addAttribute("fullName", null);
            model.addAttribute("isAdmin", false);
            logger.debug("No authenticated user");
        }
    }
}