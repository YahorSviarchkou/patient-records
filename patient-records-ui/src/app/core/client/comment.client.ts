import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {CommentModel} from "../model/comment.model";

@Injectable({
  providedIn: 'root'
})
export class CommentClient {

  constructor(private http: HttpClient) {
  }

  getCommentsByPatientId(patientId: number) : Observable<CommentModel[]>{
    return this.http.get<CommentModel[]>('http://localhost:8080/comments/' + patientId)
  }

  getCommentsByPatientAndUserId(patientId: number, doctorId: number) : Observable<CommentModel[]>{
    return this.http.get<CommentModel[]>('http://localhost:8080/comments/' + patientId + "/" + doctorId)
  }
}
