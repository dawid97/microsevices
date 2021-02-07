import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/services/auth/auth.service';
import { TokenStorageService } from 'src/app/services/token-storage/token-storage.service';
import jwt_decode from 'jwt-decode';
import { CurrentUser } from 'src/app/models/CurrentUser';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  form: any = {};
  isLoggedIn = false;
  isLoginFailed = false;
  errorMessage = '';
  roles: string[] = [];
  currentUser: CurrentUser;

  constructor(private authService: AuthService, private tokenStorage: TokenStorageService, private router: Router) { }

  ngOnInit(): void {
    if (this.tokenStorage.getToken()) {
      this.isLoggedIn = true;
      this.roles = this.tokenStorage.getUser().authorities;
    }
    this.currentUser = new CurrentUser();
  }

  onSubmit(): void {
    window.sessionStorage.setItem('windowReloaded', 'false');
    this.login();
  }

  login(): void {
    this.authService.login(this.form).subscribe(
      data => {
        this.tokenStorage.saveToken(data.jwt);
        this.prepareUser(data);
        this.isLoginFailed = false;
        this.isLoggedIn = true;
        this.reloadPage();
      },
      (error) => {
        this.matchException(error);
        this.isLoginFailed = true;
      }
    );
  }

  matchException(error: any): void {
    if (error.error.email) {
      this.errorMessage = error.error.email;
    }
    if (error.error.password) {
      this.errorMessage += ' or ' + error.error.password;
    }
  }

  prepareUser(data: any): void {
    const decodedToken = this.getDecodedAccessToken(data.jwt);
    this.currentUser.token = data.jwt;
    this.currentUser.email = decodedToken.sub;
    this.currentUser.authorities = decodedToken.authorities;
    this.tokenStorage.saveUser(this.currentUser);
    this.roles = this.tokenStorage.getUser().authorities;
  }

  okClicked(): void {
    this.router.navigate(['/home']);
  }

  getDecodedAccessToken(token: string): any {
    try {
      return jwt_decode(token);
    }
    catch (Error) {
      return null;
    }
  }

  reloadPage(): void {
    window.location.reload();
  }
}
