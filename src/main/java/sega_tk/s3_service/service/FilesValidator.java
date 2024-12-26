package sega_tk.s3_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilesValidator {

    private static final long MAX_FILE_SIZE = 5242816;

    public void checkImageSizeExceeded(MultipartFile file) {
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new MaxUploadSizeExceededException(MAX_FILE_SIZE);
        }
    }

    public void checkListCapacity(List<MultipartFile> list) {
        if (list.size() > 10) {
            throw new RuntimeException("PostImageValidator. Amount images more 10");
        }
    }
}
