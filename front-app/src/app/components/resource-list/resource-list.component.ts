import { TokenStorageService } from './../../services/token-storage/token-storage.service';
import { CurrentUser } from './../../models/CurrentUser';
import { ResourceService } from './../../services/resource/resource.service';
import { Resource } from './../../models/Resource';
import { Component, HostListener, ViewChild, OnInit } from '@angular/core';
import { WavesModule, TableModule, IconsModule, MdbTableDirective } from 'angular-bootstrap-md';
import { Router } from '@angular/router';

@Component({
  selector: 'app-resource-list',
  templateUrl: './resource-list.component.html',
  styleUrls: ['./resource-list.component.css']
})
export class ResourceListComponent implements OnInit {

  @ViewChild(MdbTableDirective, { static: true }) mdbTable: MdbTableDirective;

  currentUser: CurrentUser;
  resources: Resource[] = [];
  headElements = ['name', 'type'];
  searchText = '';
  previous: string;
  selectedItem: number;

  constructor(private resourceService: ResourceService, private tokenStorageService: TokenStorageService, private router: Router) { }

  @HostListener('input') oninput(): void {
    this.searchItems();
  }

  addResourceClicked(): void {
    this.router.navigate(['/resources/add-resource']);
  }

  ngOnInit(): void {
    this.findResources();
    this.currentUser = this.tokenStorageService.getUser();
  }

  findResources(): void {
    this.resourceService.getResources().subscribe(
      data => {
        this.resources = data;
        this.mdbTable.setDataSource(this.resources);
        this.previous = this.mdbTable.getDataSource();
      },
      err => {
        this.resources = JSON.parse(err.error).message;
      }
    );
  }

  searchItems(): void {
    const prev = this.mdbTable.getDataSource();
    if (!this.searchText) {
      this.mdbTable.setDataSource(this.previous);
      this.resources = this.mdbTable.getDataSource();
    }
    if (this.searchText) {
      this.resources = this.mdbTable.searchLocalDataBy(this.searchText);
      this.mdbTable.setDataSource(prev);
    }
  }

  deleteResourceClicked(): void {
    this.deleteResource();
  }

  deleteResource(): void {
    this.resourceService.deleteResource(this.selectedItem).subscribe((data: any) => {
      this.reloadPage();
    }, (err: any) => { });
  }

  reloadPage(): void {
    window.location.reload();
  }

  slotsClicked(id: number): void {
    const path = '/resources/' + id + '/slots';
    this.router.navigate([path]);
  }
}
