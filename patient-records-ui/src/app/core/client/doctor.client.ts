import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {PatientModel} from "../model/patient.model";
import {Observable} from "rxjs";
import {CommentModel} from "../model/comment.model";
import {DoctorModel} from "../model/doctor.model";
import {DoctorRequestModel} from "../model/doctor-request.model";

@Injectable({
  providedIn: 'root'
})
export class DoctorClient {

  constructor(private http: HttpClient) {
  }

  getDoctors() : Observable<DoctorModel[]>{
    return this.http.get<DoctorModel[]>('http://localhost:8080/doctor-details')
  }

  updateDoctor(doctorModel: DoctorModel) : Observable<DoctorModel>{
    return this.http.put<DoctorModel>('http://localhost:8080/doctor-details', doctorModel)
  }

  createDoctor(doctorRequestModel: DoctorRequestModel) : Observable<DoctorModel>{
    return this.http.post<DoctorModel>('http://localhost:8080/doctor-details', doctorRequestModel)
  }

  getDoctorById(doctorId: number) : Observable<DoctorModel>{
    return this.http.get<DoctorModel>('http://localhost:8080/doctor-details/' + doctorId)
  }
}
