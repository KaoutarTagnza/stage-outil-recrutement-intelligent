package com.recrutementia.service;

import com.recrutementia.dto.AuthResponse;
import com.recrutementia.dto.LoginRequest;
import com.recrutementia.dto.RegisterRequest;
import com.recrutementia.model.*;
import com.recrutementia.repository.CandidatRepository;
import com.recrutementia.repository.UtilisateurRepository;
import com.recrutementia.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UtilisateurRepository utilisateurRepository;
    private final CandidatRepository candidatRepository;
    private final JournalisationService journalisationService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthResponse register(RegisterRequest req) {
        if (utilisateurRepository.existsByEmail(req.getEmail())) {
            throw new IllegalStateException("Cet email est deja utilise");
        }

        Utilisateur u = new Utilisateur();
        u.setNom(req.getNom());
        u.setEmail(req.getEmail());
        u.setMotDePasse(passwordEncoder.encode(req.getMotDePasse()));
        u.setRole(req.getRole());
        u = utilisateurRepository.save(u);

        if (req.getRole() == Role.CANDIDAT) {
            Candidat c = new Candidat();
            c.setUtilisateur(u);
            c.setTelephone(req.getTelephone());
            candidatRepository.save(c);
        }

        journalisationService.journaliser(u, "Creation de compte (" + u.getRole()
                + ") - Consentement traitement donnees: " + req.isConsentementDonnees());

        String token = jwtUtil.generateToken(u.getEmail(), u.getRole().name());
        return new AuthResponse(token, u.getEmail(), u.getRole().name());
    }

    public AuthResponse login(LoginRequest req) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getMotDePasse()));

        Utilisateur u = utilisateurRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new IllegalStateException("Utilisateur introuvable"));

        journalisationService.journaliser(u, "Connexion");

        String token = jwtUtil.generateToken(u.getEmail(), u.getRole().name());
        return new AuthResponse(token, u.getEmail(), u.getRole().name());
    }
}