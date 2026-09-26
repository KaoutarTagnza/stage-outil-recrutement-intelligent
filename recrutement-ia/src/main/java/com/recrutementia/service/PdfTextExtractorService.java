package com.recrutementia.service;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class PdfTextExtractorService {

    public String extraireTexte(String cheminFichier) {
        try (PDDocument document = Loader.loadPDF(new File(cheminFichier))) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        } catch (IOException e) {
            throw new IllegalStateException("Impossible de lire le contenu du PDF", e);
        }
    }
}