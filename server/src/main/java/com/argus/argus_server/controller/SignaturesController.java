package com.argus.argus_server.controller;

import com.argus.argus_server.model.Signature;
import com.argus.argus_server.repository.SignatureRepository;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/signatures")
public class SignaturesController {
    
    private final SignatureRepository signatureRepository;

    public SignaturesController(SignatureRepository signatureRepository) {
        this.signatureRepository = signatureRepository;
    }

    @GetMapping
    public List<Signature> getAllSignatures() {
        return signatureRepository.findAll();
    }

    @GetMapping("/{id}")
    public Signature getSignatureById(@PathVariable Integer id) {
        return signatureRepository.findById(id).orElse(null);
    }

    @GetMapping("/package/{packageName}")
    public List<Signature> getByPackageName(@PathVariable String packageName) {
        return signatureRepository.findByPackageName(packageName);
    }

    @GetMapping("/hash/{certificate}")
    public List<Signature> getByCertHash(@PathVariable String certHash) {
        return signatureRepository.findByCertHash(certHash);
    }

    @PostMapping
    public Signature createSignature(@RequestBody Signature signature) {
        return signatureRepository.save(signature);
    }

}
