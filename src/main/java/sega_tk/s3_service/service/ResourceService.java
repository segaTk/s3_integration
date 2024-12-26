package sega_tk.s3_service.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sega_tk.s3_service.model.Resource;
import sega_tk.s3_service.repository.ResourceRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ResourceService {

    private final ResourceRepository resourceRepository;

    public Resource findById(Long id) {
        Optional<Resource> resource = resourceRepository.findById(id);
        return resource.orElseThrow(
                () -> new EntityNotFoundException("Resource service. Resource not found. id: " + id));
    }

    public void deleteResource(Long id) {
        resourceRepository.deleteById(id);
    }

    public void saveResources(List<Resource> resources) {
        resourceRepository.saveAll(resources);
    }
}