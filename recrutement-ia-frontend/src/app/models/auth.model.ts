export interface LoginRequest {
  email: string;
  motDePasse: string;
}

export interface RegisterRequest {
  nom: string;
  email: string;
  motDePasse: string;
  role: 'ADMIN' | 'RECRUTEUR' | 'CANDIDAT';
  telephone?: string;
  consentementDonnees: boolean;
}

export interface AuthResponse {
  token: string;
  email: string;
  role: string;
}