import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {PatientModel} from "../model/patient.model";
import {ChangePasswordModel} from "../model/change-password.model";
import {CommentModel} from "../model/comment.model";

@Injectable({
  providedIn: 'root'
})
export class PatientClient {

  constructor(private http: HttpClient) {
  }

  getAllPatients(fio:string, minBirthday:string, maxBirthday: string, minCreation: string, maxCreation: string)
    : Observable<PatientModel[]>{
    let queryParams = {
      "FIO":fio,
      "minBirthday":minBirthday,
      "maxBirthday":maxBirthday,
      "minCreation":minCreation,
      "maxCreation":maxCreation
    };
    return this.http.get<PatientModel[]>('http://localhost:8080/patient-cards', {params:queryParams})
  }

  updatePatient(patientModel: PatientModel) : Observable<PatientModel>{
    return this.http.put<PatientModel>('http://localhost:8080/patient-cards', patientModel)
  }

  createPatient(patientModel: PatientModel) : Observable<PatientModel>{
    return this.http.post<PatientModel>('http://localhost:8080/patient-cards', patientModel)
  }

  writeComment(commentModel: CommentModel, patientId: number, userId: number) : Observable<PatientModel>{
    return this.http.post<PatientModel>('http://localhost:8080/patient-cards/' + patientId + '/' + userId + '/comments', commentModel)
  }

  getPatientById(patientId: number) : Observable<PatientModel>{
    return this.http.get<PatientModel>('http://localhost:8080/patient-cards/' + patientId)
  }

  deletePatientById(patientId: number) : Observable<PatientModel>{
    return this.http.delete<PatientModel>('http://localhost:8080/patient-cards/' + patientId)
  }
}
