package com.example.naumenProject.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.example.naumenProject.models.Team;
import com.example.naumenProject.repositories.ProjectRepository;
import com.example.naumenProject.repositories.TeamRepository;
import com.example.naumenProject.services.TeamService;
import com.example.naumenProject.services.UserService;
import jakarta.servlet.Registration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.naumenProject.models.Project;
import com.example.naumenProject.models.User;
import com.example.naumenProject.repositories.UserRepository;
import com.example.naumenProject.services.ProjectService;

@Controller
public class WebController {
    private final UserRepository userRepository;
    public final ProjectRepository projectRepository;
    private final ProjectService projectService;
    private final TeamService teamService;
    private final UserService userService;

    @Autowired
    public WebController(UserRepository userRepository, ProjectRepository projectRepository, ProjectService projectService, TeamService teamService, UserService userService) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.projectService = projectService;
        this.teamService = teamService;
        this.userService = userService;
    }

    @GetMapping(value = "/")
    public String getMainPage(Model model, Authentication authentication, Registration registration) {
        String username = authentication.getName();

        User currentUser = userRepository.findUserByUsername(username);

        model.addAttribute("user", currentUser);

        return "index";
    }

    @GetMapping(value = "/projects")
    public String getProjectsPage(Model model, Authentication authentication) {
        String username = authentication.getName();

        User currentUser = userRepository.findUserByUsername(username);

        var projects = projectService.getAllProjects();

        model.addAttribute("title", "Проекты");
        model.addAttribute("user", currentUser);
        model.addAttribute("projects", projects);

        return "projects";
    }

    @GetMapping(value = "/projectsByUser")
    public String getProjectsByUser(Model model, Authentication authentication) {
        String username = authentication.getName();

        User currentUser = userRepository.findUserByUsername(username);

        var userProjects = projectService.getProjectsByUser(username);

        model.addAttribute("title", "Проекты от " + currentUser.getUsername());
        model.addAttribute("user", currentUser);
        model.addAttribute("projects", userProjects);

        return "projects";
    }

    @PostMapping(value = "/createProject")
    public String createProject(@RequestParam("name") String name, @RequestParam("description") String description,
                                Authentication authentication) {
        String username = authentication.getName();

        User currentUser = userRepository.findUserByUsername(username);

        Project project = new Project(UUID.randomUUID().getMostSignificantBits(), username, name, description, "", "", "", "",
                "");

        projectService.createProject(project);

        return "redirect:/projects";
    }

//    @PostMapping(value = "/updateProject")
//    public String updateProject(@RequestParam("name") String name, @RequestParam("description") String description,
//                                Authentication authentication) {
//        String username = authentication.getName();
//
//        User currentUser = userRepository.findUserByUsername(username);
//
//        Project project = new Project(UUID.randomUUID().getMostSignificantBits(), username, name, description, "", "", "", "",
//                "");
//
//        projectService.createProject(project);
//
//        return "redirect:/projects";
//    }

    // Не работает, ПОПРАВИТЬ удаление только TeamLead
    @PostMapping(value = "/deleteProject")
    public String deleteProject(@RequestParam("id") Long id, Authentication authentication) {
        projectService.deleteProject(id);

        return "redirect:/projects";
    }


    @PostMapping(value = "/uploadArchive")
    public ResponseEntity<String> uploadArchive(@RequestParam("id") Long id, @RequestParam("project_archive") MultipartFile archiveFile,
            Authentication authentication) {
        var project = projectService.getProjectById(id);

        if (project != null && !archiveFile.isEmpty()) {
            String contentType = archiveFile.getContentType();
            if (!"application/zip".equals(contentType) && !"application/x-zip-compressed".equals(contentType)) {
                return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("Only zip files are allowed.");
            }

            String uploadDir = project.getUploadDir();
            String originalFilename = archiveFile.getOriginalFilename();
            String filePath = uploadDir + originalFilename;

            try {
                File directory = new File(uploadDir);
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                File file = new File(filePath);
                archiveFile.transferTo(file);

                project.setProjectArchivePath(filePath);
                projectService.updateProject(project);

                return ResponseEntity.status(HttpStatus.OK).body("File uploaded successfully.");
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading file.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid project ID or empty file.");
        }
    }

    @GetMapping(value = "/ratings")
    public String getRatingsPage(Model model, Authentication authentication) {
        String username = authentication.getName();

        User currentUser = userRepository.findUserByUsername(username);
        var projects = projectService.getProjectsSortedByRating();

        model.addAttribute("user", currentUser);
        model.addAttribute("projects", projects);

        return "ratings";
    }

    @PostMapping(value = "/updateRating")
    public String updateRating(@RequestParam("projectName") String projectName, @RequestParam("rating") Integer rating,
            Authentication authentication) {
        var project = projectService.getProjectByName(projectName);

        if (project != null) {
            project.setProjectRating(rating);
            projectService.updateProject(project);
        }

        return "redirect:/ratings";
    }

    @GetMapping(value = "/search")
    public String search(@RequestParam(value = "query", required = false) String query, Model model,
            Authentication authentication) {
        String username = authentication.getName();

        User currentUser = userRepository.findUserByUsername(username);

        List<Project> filteredProjects;
        if (query != null && !query.isEmpty()) {
            filteredProjects = projectService.searchProjects(query);
        } else {
            filteredProjects = projectService.getAllProjects();
        }

        model.addAttribute("user", currentUser);
        model.addAttribute("projects", filteredProjects);

        return "search";
    }

    @GetMapping(value = "/play")
    public String play(@RequestParam("id") Long id, Model model, Authentication authentication) {
        String username = authentication.getName();
        User currentUser = userRepository.findUserByUsername(username);
        var project = projectService.getProjectById(id);

        try {
            File archiveFile = new File(project.getProjectArchivePath());
            File destDir = new File(project.getUploadDir() + "unzipped");

            if (!destDir.exists()) {
                destDir.mkdirs();
            }

            byte[] buffer = new byte[1024];
            ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(archiveFile));
            ZipEntry entry = zipInputStream.getNextEntry();

            while (entry != null) {
                String entryPath = destDir.getAbsolutePath() + File.separator + entry.getName();
                File entryFile = new File(entryPath);

                if (entry.isDirectory()) {
                    entryFile.mkdirs();
                } else {
                    FileOutputStream fos = new FileOutputStream(entryFile);
                    int len;
                    while ((len = zipInputStream.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                    fos.close();
                }
                zipInputStream.closeEntry();
                entry = zipInputStream.getNextEntry();
            }
            zipInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/games/" + id + "/unzipped/index.html";
    }

    @PostMapping(value = "/uploadGameplayVideo")
    public ResponseEntity<String> uploadGameplayVideo(@RequestParam("id") Long id, @RequestParam("video_gameplay") MultipartFile videoFile,
                                                Authentication authentication) {
        var project = projectService.getProjectById(id);

        if (project != null && !videoFile.isEmpty()) {
            String contentType = videoFile.getContentType();
            assert contentType != null;
            if (!contentType.equalsIgnoreCase("mp4")) {
                return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("Only mp4 files are allowed.");
            }

            String uploadDir = project.getUploadDir();
            String originalFilename = videoFile.getOriginalFilename();
            String filePath = uploadDir + originalFilename;

            try {
                File directory = new File(uploadDir);
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                File file = new File(filePath);
                videoFile.transferTo(file);

                project.setGameplayVideo(filePath);
                projectService.updateProject(project);

                return ResponseEntity.status(HttpStatus.OK).body("File uploaded successfully.");
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading file.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid project ID or empty file.");
        }
    }

    @PostMapping(value = "/uploadCover")
    public ResponseEntity<String> uploadCover(@RequestParam("id") Long id, @RequestParam("video_gameplay") MultipartFile coverFile,
                                                      Authentication authentication) {
        var project = projectService.getProjectById(id);

        if (project != null && !coverFile.isEmpty()) {
            String contentType = coverFile.getContentType();
            assert contentType != null;
            if (!contentType.equalsIgnoreCase("svg")) {
                return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("Only svg files are allowed.");
            }

            String uploadDir = project.getUploadDir();
            String originalFilename = coverFile.getOriginalFilename();
            String filePath = uploadDir + originalFilename;

            try {
                File directory = new File(uploadDir);
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                File file = new File(filePath);
                coverFile.transferTo(file);

                project.setGameplayVideo(filePath);
                projectService.updateProject(project);

                return ResponseEntity.status(HttpStatus.OK).body("File uploaded successfully.");
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading file.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid project ID or empty file.");
        }
    }

    @PostMapping(value = "/uploadRepositoryLink")
    public ResponseEntity<String> uploadRepositoryLink(@RequestParam("id") Long id, @RequestParam("project_archive") MultipartFile archiveFile,
                                                Authentication authentication) {
        var project = projectService.getProjectById(id);

        if (project != null && !archiveFile.isEmpty()) {
            String contentType = archiveFile.getContentType();
            if (!"application/zip".equals(contentType) && !"application/x-zip-compressed".equals(contentType)) {
                return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("Only zip files are allowed.");
            }

            String uploadDir = project.getUploadDir();
            String originalFilename = archiveFile.getOriginalFilename();
            String filePath = uploadDir + originalFilename;

            try {
                File directory = new File(uploadDir);
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                File file = new File(filePath);
                archiveFile.transferTo(file);

                project.setProjectArchivePath(filePath);
                projectService.updateProject(project);

                return ResponseEntity.status(HttpStatus.OK).body("File uploaded successfully.");
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading file.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid project ID or empty file.");
        }
    }

    @PostMapping(value = "/chooseTeamRole")
    public String chooseTeamRole(@RequestParam("team_role") String roleInProject,
                                 Authentication authentication) {
//        String username = authentication.getName();
//
//        User currentUser = userRepository.findUserByUsername(username);

//        User myUser = new User(roleInProject);
//
//        userService.createUser(myUser);
//
//        return "redirect:/";

        String username = authentication.getName();
        var user = userService.getUserByUsername(username);

        if (user != null) {
            user.setRoleInProject(roleInProject);
            userService.createUser(user);
        }

        return "redirect:/";
    }
}
