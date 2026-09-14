import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { inject } from '@angular/core';

export const adminGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);

  const router = inject(Router);

  const role = authService.getRole();

  if (role === 'ROLE_ADMIN') {
    return true;
  }

  router.navigate(['/']);

  return false;
};
