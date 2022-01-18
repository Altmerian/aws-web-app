package com.epam.cloudx.pavelsh.awsapp.rds.dao;

import com.epam.cloudx.pavelsh.awsapp.rds.entity.ImageEntityModel;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageJpaRepository extends JpaRepository<ImageEntityModel, Long> {

  List<ImageEntityModel> findByName(String name);

  @Query(nativeQuery = true, value = "SELECT * FROM image ORDER BY RAND() limit 1;")
  Optional<ImageEntityModel> findRandomEntity();
}
