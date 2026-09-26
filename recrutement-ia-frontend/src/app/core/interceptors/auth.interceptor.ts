import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';

// Ajoute automatiquement le token JWT dans le header Authorization de chaque requete
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const token = authService.getToken();

  if (token) {
    const clone = req.clone({
      setHeaders: { Authorization: `Bearer ${token}` }
    });
    return next(clone);
  }

  return next(req);
};