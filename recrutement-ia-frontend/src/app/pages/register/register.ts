import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { ToastService } from '../../core/services/toast.service';
import { RegisterRequest } from '../../models/auth.model';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './register.html',
  styleUrl: './register.css'
})
export class Register {
  data: RegisterRequest = {
    nom: '',
    email: '',
    motDePasse: '',
    role: 'CANDIDAT',
    telephone: '',
    consentementDonnees: false
  };

  erreur = signal('');
  chargement = signal(false);

  constructor(
    private authService: AuthService,
    private router: Router,
    private toast: ToastService
  ) {}

  onSubmit(): void {
    this.erreur.set('');
    this.chargement.set(true);

    this.authService.register(this.data).subscribe({
      next: () => {
        this.chargement.set(false);
        this.toast.success('Compte créé avec succès, bienvenue !');
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        this.chargement.set(false);
        this.erreur.set(err.error?.erreur || err.error?.message || 'Erreur lors de l\'inscription. Vérifiez que le mot de passe fait au moins 8 caractères et que vous avez coché la case de consentement.');
      }
    });
  }
}