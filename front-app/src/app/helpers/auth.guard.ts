import { TokenStorageService } from './../services/token-storage/token-storage.service';
import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {


  constructor(private tokenStorageService: TokenStorageService, private router: Router) {

  }

  canActivate(): boolean {
    if (!this.tokenStorageService.isTokenExpired()) {
      return true;
    } else {
      this.router.navigateByUrl('');
      return false;
    }
  }

}
