package com.recrutementia.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${app.upload.dir}")
    private String uploadDir;

    public String storeCv(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalStateException("Le fichier est vide");
        }

        String original = StringUtils.cleanPath(file.getOriginalFilename());
        if (!original.toLowerCase().endsWith(".pdf")) {
            throw new IllegalStateException("Seuls les fichiers PDF sont acceptes");
        }

        try {
            Path dossier = Paths.get(uploadDir);
            Files.createDirectories(dossier);

            String nomFichier = UUID.randomUUID() + "_" + original;
            Path destination = dossier.resolve(nomFichier);
            Files.copy(file.getInputStream(), destination);

            return destination.toString();
        } catch (IOException e) {
            throw new IllegalStateException("Impossible d'enregistrer le fichier CV", e);
        }
    }
}