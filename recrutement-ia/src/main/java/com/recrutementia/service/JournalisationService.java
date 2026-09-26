package com.recrutementia.service;

import com.recrutementia.model.LogAction;
import com.recrutementia.model.Utilisateur;
import com.recrutementia.repository.LogActionRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
@RequiredArgsConstructor
public class JournalisationService {

    private final LogActionRepository logActionRepository;

    public void journaliser(Utilisateur u, String action) {
        LogAction log = new LogAction();
        log.setUtilisateur(u);
        log.setAction(action);
        log.setAdresseIp(obtenirAdresseIp());
        log.setDetails(obtenirUserAgent());
        logActionRepository.save(log);
    }

    private String obtenirAdresseIp() {
        HttpServletRequest request = obtenirRequete();
        if (request == null) return null;

        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isBlank()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    private String obtenirUserAgent() {
        HttpServletRequest request = obtenirRequete();
        return request != null ? request.getHeader("User-Agent") : null;
    }

    private HttpServletRequest obtenirRequete() {
        ServletRequestAttributes attrs =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attrs != null ? attrs.getRequest() : null;
    }
}