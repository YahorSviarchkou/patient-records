import {Injectable} from "@angular/core";
import {HttpClient, HttpParams} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class ImageClient {

  constructor(private http: HttpClient) {
  }

  uploadFile(file: any, patientId: number) {
    return this.http.post('http://localhost:8080/upload/' + patientId, file,
      {observe: 'response', responseType: 'blob'})
  }

  getFile(patientId: number) {
    return this.http.get('http://localhost:8080/files/' + patientId,
      {observe: 'response', responseType: 'blob'})
  }
}
