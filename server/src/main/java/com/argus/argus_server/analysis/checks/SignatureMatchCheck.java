package com.argus.argus_server.analysis.checks;

import com.argus.argus_server.analysis.RiskCheck;
import com.argus.argus_server.analysis.RiskFactor;
import com.argus.argus_server.dto.ScannedApp;
import com.argus.argus_server.model.Signatures;
import com.argus.argus_server.repository.SignaturesRepository;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Known stalkerware: the app matches a signature in the database. */
@Component
@Order(1)
public class SignatureMatchCheck implements RiskCheck {

    private final SignaturesRepository signatures;

    public SignatureMatchCheck(SignaturesRepository signatures) {
        this.signatures = signatures;
    }

    @Override
    public List<RiskFactor> evaluate(ScannedApp app) {
        List<Signatures> candidates = new ArrayList<>(signatures.findByPackageName(app.packageName()));
        if (app.certHash() != null) {
            candidates.addAll(signatures.findByCertHash(app.certHash()));
        }
        return candidates.stream()
                .filter(signature -> signature.matchesApp(app))
                .findFirst()
                .map(signature -> List.of(new RiskFactor(100,
                        "Matches known stalkerware (" + Objects.requireNonNullElse(signature.getAppFamily(), "unknown family") + ")",
                        signature.getSignaturesID())))
                .orElse(List.of());
    }
}
