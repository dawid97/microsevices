import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  form: any = {};
  isSuccessful = false;
  isSignUpFailed = false;
  errorMessage = '';


  constructor(private authService: AuthService, private router: Router) { }

  ngOnInit(): void {
  }

  okClicked(): void {
    this.router.navigate(['/login']);
  }

  onSubmit(): void {
    this.register();
  }

  register(): void {
    this.authService.register(this.form).subscribe(
      data => {
        this.isSuccessful = true;
        this.isSignUpFailed = false;
      },
      (error) => {
        this.matchException(error);
        this.isSignUpFailed = true;
      }
    );
  }

  matchException(error: any): void {
    if (error.error.confirmPassword) {
      this.errorMessage = error.error.confirmPassword;
    }
    if (error.error.email) {
      this.errorMessage = error.error.email;
    }
  }
}

