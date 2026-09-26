import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class Login {
  email = '';
  motDePasse = '';
  erreur = signal('');
  chargement = signal(false);

  constructor(private authService: AuthService, private router: Router) {}

  onSubmit(): void {
    this.erreur.set('');
    this.chargement.set(true);

    this.authService.login({ email: this.email, motDePasse: this.motDePasse }).subscribe({
      next: () => {
        this.chargement.set(false);
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        this.chargement.set(false);
        this.erreur.set(err.error?.erreur || 'Email ou mot de passe incorrect');
      }
    });
  }
}