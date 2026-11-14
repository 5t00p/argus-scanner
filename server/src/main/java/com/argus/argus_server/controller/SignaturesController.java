package com.argus.argus_server.controller;

import com.argus.argus_server.model.Signatures;
import com.argus.argus_server.repository.SignaturesRepository;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/signatures")
public class SignaturesController {
    
    private final SignaturesRepository signatureRepository;

    public SignaturesController(SignaturesRepository signatureRepository) {
        this.signatureRepository = signatureRepository;
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

    @GetMapping("/hash/{certificate}")
    public List<Signatures> getByCertHash(@PathVariable String certHash) {
        return signatureRepository.findByCertHash(certHash);
    }

    @PostMapping
    public Signature createSignature(@RequestBody Signature signature) {
        return signatureRepository.save(signature);
    }

}
