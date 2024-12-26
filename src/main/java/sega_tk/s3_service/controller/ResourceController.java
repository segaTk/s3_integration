package sega_tk.s3_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sega_tk.s3_service.service.FilesService;

import java.util.List;

@Validated
@RestController
@RequestMapping("api/v1/resources")
@RequiredArgsConstructor
public class ResourceController {

    private final FilesService filesService;

    @PostMapping()
    public void addFiles(@RequestPart("files") List<MultipartFile> files) {
        filesService.uploadFiles(files);
    }

    @DeleteMapping("/{resourceId}")
    public void deleteFile(@PathVariable Long resourceId) {
        filesService.deleteImage(resourceId);
    }
}
