import { Injectable, signal } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class ToastService {
  message = signal<string | null>(null);
  type = signal<'success' | 'error'>('success');
  private timeoutId: any;

  success(msg: string): void {
    this.show(msg, 'success');
  }

  error(msg: string): void {
    this.show(msg, 'error');
  }

  private show(msg: string, type: 'success' | 'error'): void {
    clearTimeout(this.timeoutId);
    this.message.set(msg);
    this.type.set(type);
    this.timeoutId = setTimeout(() => this.message.set(null), 3500);
  }
}