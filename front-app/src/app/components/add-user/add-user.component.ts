import { CurrentUser } from './../../models/CurrentUser';
import { TokenStorageService } from './../../services/token-storage/token-storage.service';
import { UserService } from 'src/app/services/user/user.service';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { User } from 'src/app/models/User';


@Component({
  selector: 'app-add-user',
  templateUrl: './add-user.component.html',
  styleUrls: ['./add-user.component.css']
})
export class AddUserComponent implements OnInit {

  form: any = {};
  isSuccessful = false;
  selectedRole: string;
  information: string;
  modalTitle: string;
  currentUser: CurrentUser;

  roles = ['Admin', 'Customer', 'Resource', 'Staff'];

  constructor(
    private router: Router,
    private userService: UserService,
    private tokenStorageService: TokenStorageService
  ) { }

  ngOnInit(): void {
    this.currentUser = this.tokenStorageService.getUser();
  }

  onSubmit(): void {
    this.modalTitle = 'Information';
    if (!this.onlyDigits(this.form.ssn)) {
      this.information = '\nSsn can only contain numbers';
      return;
    }
    if (this.currentUser.authorities.includes('ROLE_ADMIN')) {
      this.adminOperations();
    } else {
      this.staffOperations();
    }
  }

  staffOperations(): void {
    this.addCustomer();
  }

  adminOperations(): void {
    if (this.roles.includes(this.selectedRole)) {
      this.addUser();
    } else {
      this.information = '\nSelect role from list';
    }
  }

  onlyDigits(str: string): boolean {
    for (let i = 0; i < str.length; i++) {
      if (!(str.charAt(i) >= '0' && str.charAt(i) <= '9')) {
        return false;
      }
    }
    return true;
  }

  setDefaultInformation(): void {
    this.information = '\nPlease fill the form correctly';
  }

  prepareUser(): User {
    const user: User = new User();
    user.name = this.form.name;
    user.surname = this.form.surname;
    user.email = this.form.email;
    user.confirmPassword = this.form.confirmPassword;
    user.ssn = this.form.ssn;
    user.password = this.form.password;
    user.role = 'ROLE_' + this.form.role.toUpperCase();
    return user;
  }

  prepareCustomer(): User {
    const customer: User = new User();
    customer.name = this.form.name;
    customer.surname = this.form.surname;
    customer.ssn = this.form.ssn;
    return customer;
  }

  addUser(): void {
    this.information = '\nUser added successfully';
    const user: User = this.prepareUser();
    this.userService.addUser(user).subscribe((data: any) => {
      this.isSuccessful = true;
    },
      (error: any) => {
        this.isSuccessful = false;
        this.information = '\n';
        this.matchException(error);
      });
  }

  addCustomer(): void {
    this.information = '\nCustomer added successfully';
    const customer: User = this.prepareCustomer();
    this.userService.addCustomer(customer).subscribe((data: any) => {
      this.isSuccessful = true;
    },
      (error: any) => {
        this.isSuccessful = false;
        this.information = '\n';
        this.matchException(error);
      });
  }

  matchException(error: any): void {
    if (error.error.confirmPassword) {
      this.information += error.error.confirmPassword;
    } else if (error.error.email) {
      this.information += error.error.email;
    } else if (error.error.ssn) {
      this.information += error.error.ssn;
    }
  }

  okClicked(): void {
    if (this.isSuccessful) {
      this.router.navigate(['/users']);
    }
  }
}
