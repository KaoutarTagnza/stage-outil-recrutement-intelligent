import { Routes } from '@angular/router';
import { Login } from './pages/login/login';
import { Register } from './pages/register/register';
import { Dashboard } from './pages/dashboard/dashboard';
import { ListeOffres } from './pages/liste-offres/liste-offres';
import { DetailOffre } from './pages/detail-offre/detail-offre';
import { CreerOffre } from './pages/creer-offre/creer-offre';
import { MesOffres } from './pages/mes-offres/mes-offres';
import { MesCandidatures } from './pages/mes-candidatures/mes-candidatures';
import { CandidaturesOffre } from './pages/candidatures-offre/candidatures-offre';
import { DetailCandidature } from './pages/detail-candidature/detail-candidature';
import { Admin } from './pages/admin/admin';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  { path: '', redirectTo: '/offres', pathMatch: 'full' },
  { path: 'login', component: Login },
  { path: 'register', component: Register },
  { path: 'dashboard', component: Dashboard, canActivate: [authGuard] },
  { path: 'offres', component: ListeOffres },
  { path: 'offres/:offreId/candidatures', component: CandidaturesOffre, canActivate: [authGuard] },
  { path: 'offres/:id', component: DetailOffre },
  { path: 'creer-offre', component: CreerOffre, canActivate: [authGuard] },
  { path: 'mes-offres', component: MesOffres, canActivate: [authGuard] },
  { path: 'mes-candidatures', component: MesCandidatures, canActivate: [authGuard] },
  { path: 'candidatures/:id', component: DetailCandidature, canActivate: [authGuard] },
  { path: 'admin', component: Admin, canActivate: [authGuard] },
  { path: '**', redirectTo: '/offres' }
];