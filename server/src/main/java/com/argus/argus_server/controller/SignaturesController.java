package com.argus.argus_server.controller;

import com.argus.argus_server.model.Signatures;
import com.argus.argus_server.repository.SignaturesRepository;

import org.springframework.web.bind.annotation.*;
import java.util.List;

import org.springframework.http.ResponseEntity;
import com.argus.argus_server.repository.FindingRepository;

@RestController
@RequestMapping("/api/signatures")
public class SignaturesController {
    
    private final SignaturesRepository signatureRepository;
    private final FindingRepository findingRepository;

    public SignaturesController(SignaturesRepository signatureRepository, FindingRepository findingRepository) {
        this.signatureRepository = signatureRepository;
        this.findingRepository = findingRepository;
    }


    @GetMapping
    public List<Signatures> getAllSignatures() {
        return signatureRepository.findAll();
    }

    @GetMapping("/{id}")
    public Signatures getSignatureById(@PathVariable Integer id) {
        return signatureRepository.findById(id).orElse(null);
    }

    @GetMapping("/package/{packageName}")
    public List<Signatures> getByPackageName(@PathVariable String packageName) {
        return signatureRepository.findByPackageName(packageName);
    }

    @GetMapping("/hash/{certHash}")
    public List<Signatures> getByCertHash(@PathVariable String certHash) {
        return signatureRepository.findByCertHash(certHash);
    }

    @PostMapping
    public Signatures createSignature(@RequestBody Signatures signature) {
        return signatureRepository.save(signature);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Signatures> updateSignature(@PathVariable Integer id, @RequestBody Signatures updatedSignature) {
    Signatures existingSignature = signatureRepository.findById(id).orElse(null);

    if (existingSignature == null) {
        return ResponseEntity.notFound().build();
    }

    if (updatedSignature.getPackageName() != null) {
        existingSignature.setPackageName(updatedSignature.getPackageName());
    }

    if (updatedSignature.getCertHash() != null) {
        existingSignature.setCertHash(updatedSignature.getCertHash());
    }

    if (updatedSignature.getType() != null) {
        existingSignature.setType(updatedSignature.getType());
    }

    if (updatedSignature.getAppFamily() != null) {
        existingSignature.setAppFamily(updatedSignature.getAppFamily());
    }

    Signatures savedSignature = signatureRepository.save(existingSignature);
    return ResponseEntity.ok(savedSignature);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSignature(@PathVariable Integer id) {
        if (!signatureRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        if (!findingRepository.findBySignaturesID(id).isEmpty()) {
            return ResponseEntity.status(409).build();
        }

        signatureRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }


}
