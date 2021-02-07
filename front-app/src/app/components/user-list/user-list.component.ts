import { UserService } from 'src/app/services/user/user.service';
import { Component, HostListener, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { MdbTableDirective } from 'angular-bootstrap-md';
import { CurrentUser } from 'src/app/models/CurrentUser';
import { User } from 'src/app/models/User';
import { TokenStorageService } from 'src/app/services/token-storage/token-storage.service';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {

  @ViewChild(MdbTableDirective, { static: true }) mdbTable: MdbTableDirective;

  currentUser: CurrentUser;
  users: User[] = [];
  headElements = ['name', 'surname', 'ssn', 'email'];
  searchText = '';
  previous: string;
  selectedItem: number;
  customers: User[] = [];

  constructor(
    private tokenStorageService: TokenStorageService,
    private router: Router,
    private userService: UserService
  ) { }

  @HostListener('input') oninput(): void {
    this.searchItems();
  }

  searchItems(): void {
    const prev = this.mdbTable.getDataSource();
    if (!this.searchText) {
      this.mdbTable.setDataSource(this.previous);
      if (this.currentUser.authorities.includes('ROLE_ADMIN')) {
        this.users = this.mdbTable.getDataSource();
      } else {
        this.customers = this.mdbTable.getDataSource();
      }
    }
    if (this.searchText) {
      if (this.currentUser.authorities.includes('ROLE_ADMIN')) {
        this.users = this.mdbTable.searchLocalDataBy(this.searchText);
      } else {
        this.customers = this.mdbTable.searchLocalDataBy(this.searchText);
      }
      this.mdbTable.setDataSource(prev);
    }
  }

  findUsers(): void {
    this.userService.getUsers().subscribe(data => {
      this.users = data;
      this.prepareData();
      this.mdbTable.setDataSource(this.users);
      this.previous = this.mdbTable.getDataSource();
    },
      error => { });
  }

  reloadPage(): void {
    window.location.reload();
  }

  prepareData(): void {
    this.users = this.users.filter(user => user.ssn !== '123456789');
  }

  ngOnInit(): void {
    this.currentUser = this.tokenStorageService.getUser();
    if (this.currentUser.authorities.includes('ROLE_ADMIN')) {
      this.findUsers();
    } else {
      this.findCustomers();
    }
  }

  findCustomers(): void {
    this.userService.getCustomers().subscribe(data => {
      this.customers = data;
      this.mdbTable.setDataSource(this.customers);
      this.previous = this.mdbTable.getDataSource();
    },
      (error: any) => { });
  }

  deleteUser(userId: number): void {
    this.userService.deleteUser(userId).subscribe(data => {
      this.reloadPage();
    },
      (error: any) => { });
  }

  deleteUserClicked(): void {
    this.deleteUser(this.selectedItem);
  }

  addUserClicked(): void {
    this.router.navigate(['users/add-user']);
  }
}
