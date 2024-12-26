package sega_tk.s3_service.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sega_tk.s3_service.config.ThreadPoolConfig;
import sega_tk.s3_service.model.Resource;

import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 s3Client;

    @Value("${services.s3.bucketName}")
    private String bucketName;

    private final ThreadPoolConfig threadPoolConfig;

    /**
     * Загрузка файлов в облачное хранилище S3
     *
     * @param files Список файлов
     * @return Список ссылок на загруженные файлы
     * @throws AmazonS3Exception Если возникает проблема при обращении к хранилищу S3
     * @throws RuntimeException  Если во время выполнения одного из потоков произойдёт ошибка
     */
    public List<Resource> uploadFiles(List<MultipartFile> files) {
        try {
            putFileToBucket(files);
        } catch (AmazonS3Exception e) {
            log.error("ERROR uploading images to Object Storage. Reason:", e);
            throw new RuntimeException("ERROR uploading images to Object Storage.");
        }
        return mapToResources(files);
    }

    public void deleteFile(String key) {
        try {
            s3Client.deleteObject(bucketName, key);
        } catch (SdkClientException e) {
            log.error("ERROR S3 Service. Failed to delete a file  Reason:", e);
            throw new RuntimeException("ERROR S3 Service. Failed to delete a file.");
        }
    }

    private void putFileToBucket(List<MultipartFile> files) {
        List<Future<String>> futures = new ArrayList<>();

        for (MultipartFile file : files) {
            Future<String> future = threadPoolConfig.executorService().submit(() -> putObjectsToS3(file));
            futures.add(future);
        }

        List<String> urls = new ArrayList<>();
        for (Future<String> future : futures) {
            try {
                String url = future.get();
                urls.add(url);
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException("One of the thread ended with exception.");
            }
        }
    }

    private String putObjectsToS3(MultipartFile file) {
        try {
            String fileName = generateUniqueName();
            var metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            s3Client.putObject(bucketName, fileName, file.getInputStream(), metadata);
            log.info("Upload Service. Added file: {} to bucket: {}", fileName, bucketName);
            return s3Client.getUrl(bucketName, fileName).toExternalForm();
        } catch (IOException e) {
            log.error("ERROR Upload Service. Error upload file to bucket: {}", bucketName, e);
            throw new RuntimeException("ERROR Upload Service. Error upload file to bucket.");
        }
    }

    private List<Resource> mapToResources(List<MultipartFile> files) {
        List<Resource> resources = new ArrayList<>();
        for (MultipartFile file : files) {
            var resource = new Resource();
            resource.setCreatedAt(LocalDateTime.parse(LocalDateTime.now().toString()));
            resource.setSize(BigInteger.valueOf(file.getSize()));
            resource.setType(file.getContentType());
            resource.setName(file.getName());
            resources.add(resource);
        }
        return resources;
    }

    private String generateUniqueName() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
