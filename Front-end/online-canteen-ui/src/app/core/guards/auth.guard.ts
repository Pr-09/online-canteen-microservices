import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';

export const authGuard: CanActivateFn = () => {
  const router = inject(Router);

  const token = localStorage.getItem('jwt');

  if (token) {
    return true;
  }

  alert('Please login first to check your order history.');

  return router.createUrlTree(['/login']);
};
