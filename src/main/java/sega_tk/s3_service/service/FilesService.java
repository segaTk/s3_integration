package sega_tk.s3_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sega_tk.s3_service.model.Resource;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilesService {

    private final ResourceService resourceService;
    private final S3Service s3Service;
    private final FilesValidator filesValidator;

    public void uploadFiles(List<MultipartFile> images) {
        validateFiles(images);
        List<Resource> resources = s3Service.uploadFiles(images);

        resourceService.saveResources(resources);
    }

    @Transactional
    public void deleteImage(Long resourceId) {
        Resource resource = resourceService.findById(resourceId);

        resourceService.deleteResource(resourceId);

        s3Service.deleteFile(resource.getName());
    }

    private void validateFiles(List<MultipartFile> images) {
        filesValidator.checkListCapacity(images);
        images.forEach(filesValidator::checkImageSizeExceeded);
    }
}
