import { ResourceService } from 'src/app/services/resource/resource.service';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-add-resource',
  templateUrl: './add-resource.component.html',
  styleUrls: ['./add-resource.component.css']
})
export class AddResourceComponent implements OnInit {

  form: any = {};
  isSuccessful = false;
  modalTitle = 'Information';
  information: string;

  constructor(
    private resourceService: ResourceService,
    private router: Router
  ) { }

  ngOnInit(): void {
  }

  okClicked(): void {
    if (this.isSuccessful) {
      this.router.navigate(['/resources']);
    }
  }

  setDefaultInformation(): void {
    this.information = '\nPlease fill the form correctly';
  }

  addResource(): void {
    this.resourceService.addResource(this.form).subscribe(
      data => {
        this.isSuccessful = true;
        this.information = '\nResource added successfully';
      },
      (error) => {
        this.isSuccessful = false;
        this.information = '\n' + error.error.message;
      }
    );
  }

  onSubmit(): void {
    this.addResource();
  }
}
