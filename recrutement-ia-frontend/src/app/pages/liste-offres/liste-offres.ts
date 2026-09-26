import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { OffreService } from '../../core/services/offre.service';
import { OffreResponse } from '../../models/offre.model';

@Component({
  selector: 'app-liste-offres',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './liste-offres.html',
  styleUrl: './liste-offres.css'
})
export class ListeOffres implements OnInit {
  offres = signal<OffreResponse[]>([]);
  chargement = signal<boolean>(true);

  constructor(private offreService: OffreService) {}

  ngOnInit(): void {
    this.offreService.listerPubliees().subscribe({
      next: (data) => {
        this.offres.set(data);
        this.chargement.set(false);
      },
      error: () => {
        this.chargement.set(false);
      }
    });
  }
}