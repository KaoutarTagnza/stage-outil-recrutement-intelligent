import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { OffreService } from '../../core/services/offre.service';
import { CandidatureService } from '../../core/services/candidature.service';
import { AuthService } from '../../core/services/auth.service';
import { ToastService } from '../../core/services/toast.service';
import { OffreResponse } from '../../models/offre.model';

@Component({
  selector: 'app-detail-offre',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './detail-offre.html',
  styleUrl: './detail-offre.css'
})
export class DetailOffre implements OnInit {
  offre = signal<OffreResponse | null>(null);
  chargement = signal(true);
  enCoursPostulation = signal(false);
  messagePostulation = signal('');

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private offreService: OffreService,
    private candidatureService: CandidatureService,
    public authService: AuthService,
    private toast: ToastService
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.offreService.obtenir(id).subscribe({
      next: (data) => {
        this.offre.set(data);
        this.chargement.set(false);
      },
      error: () => this.chargement.set(false)
    });
  }

  postuler(): void {
    const offre = this.offre();
    if (!offre) return;

    this.enCoursPostulation.set(true);
    this.messagePostulation.set('');

    this.candidatureService.postuler(offre.id).subscribe({
      next: () => {
        this.enCoursPostulation.set(false);
        this.toast.success('Candidature envoyée avec succès');
        this.router.navigate(['/mes-candidatures']);
      },
      error: (err) => {
        this.enCoursPostulation.set(false);
        this.messagePostulation.set(err.error?.erreur || 'Erreur lors de la candidature');
      }
    });
  }
}