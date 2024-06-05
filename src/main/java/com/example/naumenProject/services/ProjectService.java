package com.example.naumenProject.services;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.naumenProject.models.Project;
import com.example.naumenProject.models.ProjectRole;
import com.example.naumenProject.models.User;
import com.example.naumenProject.repositories.ProjectRepository;
import com.example.naumenProject.repositories.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProjectService {
    /**
     * Репозиторий проекта
     */
    private final ProjectRepository projectRepository;
    /**
     * Репозиторий студента
     */
    private final UserRepository userRepository;

    /**
     * Конструктор сервиса.
     *
     * @param projectRepository Репозиторий проектов.
     * @param userRepository Репозиторий студентов.
     */
    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public List<Project> searchProjects(String query) {
        return projectRepository.searchProjects(query);
    }

    public List<Project> getProjectsByUser(String username) {
        return projectRepository.getProjectsByUser(username);
    }

//    public List<Project> getProjectsByCategory(String category) {
//        return projectRepository.findProjectsByProjectCategory(category);
//    }

    public List<Project> getProjectsSortedByRating() {
        return projectRepository.getProjectsSortedByRating();
    }

    public Project getProjectByName(String name) {
        return projectRepository.getProjectByName(name);
    }

    public List<Project> getProjectsByCategory(String category) {
        return projectRepository.getProjectsByProjectCategory(category);
    }

    public List<Project> getAllProjects() {
        log.info("Getting all projects");
        var iterable = projectRepository.findAll();
        return (List<Project>) iterable;
    }
    /**
     * Получить проект по ID.
     *
     * @param id ID проекта.
     * @return Объект проекта или null, если проект не найден.
     */
    public Project getProjectById(Long id) {
        log.info("Getting project by ID: {}", id);
        return projectRepository.findById(id).orElse(null);
    }
    /**
     * Создать новый проект.
     *
     * @param project Объект проекта для создания.
     */
    public void createProject(Project project) {
        log.info("Creating a new project: {}", project);
        projectRepository.save(project);
    }
    /**
     * Обновить информацию о проекте.
     *
     * @param project Объект проекта для обновления.
     */
    public void updateProject(Project project) {
        log.info("Updating project: {}", project);
        projectRepository.save(project);
    }
    /**
     * Удалить проект по ID.
     *
     * @param id ID проекта для удаления.
     */
    public void deleteProject(Long id) {
        log.info("Deleting project by ID: {}", id);
        projectRepository.deleteById(id);
    }

    /**
     * Сохраняет изображение проекта и обновляет ссылку в базе данных.
     *
     * @param projectId  Идентификатор проекта.
     * @param coverImage Изображение проекта в виде объекта MultipartFile.
     * @return Объект проекта с обновленной ссылкой на изображение.
     */
    public Project uploadProjectCoverImage(Long projectId, MultipartFile coverImage) {
        Project project = projectRepository.findById(projectId).orElse(null);

        if (project != null && coverImage != null && !coverImage.isEmpty()) {
            try {
                String imageDirPath = "images";
                String imageName = UUID.randomUUID() + ".jpg";
                Path imagePath = Path.of(imageDirPath, imageName);

                Files.createDirectories(Path.of(imageDirPath));
                Files.copy(coverImage.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);

                project.setCoverImage(imagePath.toString());
                projectRepository.save(project);

                log.info("Uploaded cover image for project with ID: {}", projectId);
                return project;
            } catch (IOException e) {
                log.error("Error while uploading cover image: {}", e.getMessage());
            }
        }

        return null;
    }

    /**
     * Загружает видео геймплея проекта, сохраняет его в указанную директорию и обновляет ссылку в базе данных.
     *
     * @param projectId      Идентификатор проекта, для которого загружается видео геймплея.
     * @param gameplayVideo  Данные видео геймплея в виде объекта MultipartFile.
     * @return Объект проекта с обновленной ссылкой на видео геймплея.
     */
    public Project uploadGameplayVideo(Long projectId, MultipartFile gameplayVideo) {
        Project project = projectRepository.findById(projectId).orElse(null);

        if (project != null && gameplayVideo != null && !gameplayVideo.isEmpty()) {
            try {
                String tempDirPath = "videos";
                String tempVideoPath = tempDirPath + "\\" + UUID.randomUUID() + ".mp4";
                Files.createDirectories(Path.of(tempDirPath));

                Files.copy(gameplayVideo.getInputStream(), Path.of(tempVideoPath), StandardCopyOption.REPLACE_EXISTING);

                project.setGameplayVideo(tempVideoPath);
                projectRepository.save(project);

                log.info("Uploaded gameplay video for project with ID: {}", projectId);
                return project;
            } catch (IOException e) {
                log.error("Error while uploading gameplay video: {}", e.getMessage());
            }
        }

        return null;
    }

    /**
     * Редактировать проект, если студент является тимлидом и это его проект.
     *
     * @param userId ID студента.
     * @param projectId ID проекта.
     * @param updatedProject Обновленные данные проекта.
     * @return Обновленный объект проекта или null, если редактирование не выполнено.
     */
    public Project editProjectByTeamLeader(Long userId, Long projectId, Project updatedProject) {
        User user = userRepository.findById(userId).orElse(null);
        Project project = projectRepository.findById(projectId).orElse(null);

        if (user == null || project == null || !user.equals(project.getCreator())) {
            return null;
        }

        if (ProjectRole.TEAM_LEAD.getRole().equalsIgnoreCase(user.getRoleInProject().getRole())) {
            return null;
        }

        project.setProjectName(updatedProject.getProjectName());
        project.setProjectDescription(updatedProject.getProjectDescription());

        projectRepository.save(project);
        return project;
    }

    /**
     * Загружает архив проекта, разархивирует его и сохраняет ссылку в базе данных.
     *
     * @param projectId   Идентификатор проекта, для которого загружается архив.
     * @param archiveFile Данные архива в виде объекта MultipartFile.
     * @return Объект проекта с обновленной ссылкой на разархивированную папку.
     */
    public Project uploadProjectArchive(Long projectId, MultipartFile archiveFile) {
        Project project = projectRepository.findById(projectId).orElse(null);

        if (project != null && archiveFile != null && !archiveFile.isEmpty()) {
            try {
                String tempDirPath = "archives";
                String tempExtractedDirPath = tempDirPath + "\\" + UUID.randomUUID();
                Files.createDirectories(Path.of(tempExtractedDirPath));

                String archiveFileName = StringUtils.cleanPath(archiveFile.getOriginalFilename());
                Path tempArchivePath = Path.of(tempDirPath, archiveFileName);
                Files.copy(archiveFile.getInputStream(), tempArchivePath, StandardCopyOption.REPLACE_EXISTING);

                unzipArchive(tempArchivePath, tempExtractedDirPath);

                project.setProjectArchivePath(tempExtractedDirPath);
                projectRepository.save(project);

                log.info("Uploaded and extracted archive for project with ID: {}", projectId);
                return project;
            } catch (IOException e) {
                log.error("Error while uploading and extracting project archive: {}", e.getMessage());
            }
        }

        return null;
    }

    /**
     * Разархивирует ZIP-архив в указанную папку.
     *
     * @param archivePath Путь к ZIP-архиву.
     * @param extractPath Путь к директории, в которую будет произведена разархивация.
     * @throws IOException В случае ошибок ввода-вывода.
     */
    public void unzipArchive(Path archivePath, String extractPath) throws IOException {
        try (ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(archivePath))) {
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                String entryName = entry.getName();
                Path entryPath = Paths.get(extractPath, entryName);

                // Создание директорий (если это директория) и запись файла
                if (entry.isDirectory()) {
                    Files.createDirectories(entryPath);
                } else {
                    Files.copy(zipInputStream, entryPath, StandardCopyOption.REPLACE_EXISTING);
                }

                zipInputStream.closeEntry();
            }
        }
    }

}
