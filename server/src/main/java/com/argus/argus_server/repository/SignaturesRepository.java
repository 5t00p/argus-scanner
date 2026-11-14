package com.argus.argus_server.repository;

import com.argus.argus_server.model.Signatures;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SignaturesRepository extends JpaRepository<Signatures, Integer> {
    
    //Search signatures by package name
    List<Signatures> findByPackageName(String packageName);

    //Search signatures by certificate hash
    List<Signatures> findByCertHash(String certHash);

    //Search signatures by application family
    List<Signatures> findByAppFamily(String appFamily);
}
