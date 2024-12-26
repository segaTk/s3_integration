package sega_tk.s3_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sega_tk.s3_service.model.Resource;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long > {
}
