package finki.ukim.backend.file_handling.service.domain.impl;

import finki.ukim.backend.file_handling.model.domain.File;
import finki.ukim.backend.file_handling.model.enums.FileType;
import finki.ukim.backend.file_handling.model.exception.FileNotFoundException;
import finki.ukim.backend.file_handling.repository.FileRepository;
import finki.ukim.backend.file_handling.service.domain.FileService;
import finki.ukim.backend.file_handling.service.domain.FileStorageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FileServiceImpl implements FileService {
    private final FileRepository fileRepository;
    private final FileStorageService fileStorageService;

    @Override
    public List<File> findAll() {
        return fileRepository.findAll();
    }

    @Override
    public File findById(Long id) {
        return fileRepository.findById(id)
                .orElseThrow(() -> new FileNotFoundException(id));
    }

    @Override
    public File findByFileUrl(String fileUrl) {
        return fileRepository.findByFileUrl(fileUrl)
                .orElseThrow(() -> new FileNotFoundException(fileUrl));
    }

    @Override
    public List<File> findByFileType(FileType fileType) {
        return fileRepository.findByFileType(fileType);
    }

    @Override
    public File create(MultipartFile file, String directory) {
        return fileRepository.save(
                fileStorageService.save(file, directory)
        );
    }

    @Override
    public File create(MultipartFile file) {
        return create(file, null);
    }

    @Override
    public List<File> createAll(List<MultipartFile> files, String directory) {
        return fileRepository.saveAll(
                fileStorageService.saveAll(files, directory)
        );
    }

    @Override
    public List<File> createAll(List<MultipartFile> files) {
        return createAll(files, null);
    }

    @Override
    public File update(Long id, MultipartFile file, String directory) {
        File existingFile = findById(id);
        fileStorageService.delete(existingFile.getFileUrl());

        File savedFile = fileStorageService.save(file, directory);

        existingFile.setFileName(savedFile.getFileName());
        existingFile.setFileType(savedFile.getFileType());
        existingFile.setFileUrl(savedFile.getFileUrl());
        existingFile.setFileSize(savedFile.getFileSize());

        return fileRepository.save(existingFile);
    }

    @Override
    public File update(Long id, MultipartFile file) {
        return update(id, file, null);
    }

    @Override
    public File deleteById(Long id) {
        File file = findById(id);
        fileStorageService.delete(file.getFileUrl());
        fileRepository.delete(file);
        return file;
    }
}
