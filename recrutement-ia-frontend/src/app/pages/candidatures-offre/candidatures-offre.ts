import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { CandidatureService } from '../../core/services/candidature.service';
import { CandidatureResponse } from '../../models/candidature.model';

@Component({
  selector: 'app-candidatures-offre',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './candidatures-offre.html',
  styleUrl: './candidatures-offre.css'
})
export class CandidaturesOffre implements OnInit {
  candidatures = signal<CandidatureResponse[]>([]);
  chargement = signal(true);
  offreId!: number;

  constructor(private route: ActivatedRoute, private candidatureService: CandidatureService) {}

  ngOnInit(): void {
    this.offreId = Number(this.route.snapshot.paramMap.get('offreId'));
    this.candidatureService.listerCandidaturesOffre(this.offreId).subscribe({
      next: (data) => {
        this.candidatures.set(data);
        this.chargement.set(false);
      },
      error: () => this.chargement.set(false)
    });
  }
}