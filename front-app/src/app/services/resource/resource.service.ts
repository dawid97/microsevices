import { Resource } from './../../models/Resource';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ResourceService {

  BASE_PATH = 'http://localhost:8765/api/resource-service/resources';

  constructor(private http: HttpClient) { }

  getResources(): Observable<Resource[]> {
    return this.http.get<Resource[]>(this.BASE_PATH);
  }

  getResource(id: number): Observable<Resource> {
    return this.http.get<Resource>(this.BASE_PATH + '/' + id);
  }

  deleteResource(id: number): any {
    return this.http.delete(this.BASE_PATH + '/' + id);
  }

  addResource(resource: Resource): any {
    return this.http.post(this.BASE_PATH, { name: resource.name, type: resource.type });
  }
}
