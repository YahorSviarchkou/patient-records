import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {UserModel} from "../model/user.model";
import {Observable} from "rxjs";
import {ChangePasswordModel} from "../model/change-password.model";
import {MessageModel} from "../model/message.model";

@Injectable({
  providedIn: 'root'
})
export class UserClient {

  constructor(private http: HttpClient) {
  }

  getUsers() : Observable<UserModel[]>{
    return this.http.get<UserModel[]>('http://localhost:8080/users')
  }

  getUserById(userId: number) : Observable<UserModel[]>{
    let queryParams = {"id":userId};
    return this.http.get<UserModel[]>('http://localhost:8080/users', {params:queryParams})
  }

  getUserByLogin(login: string) : Observable<UserModel[]>{
    let queryParams = {"login":login};
    return this.http.get<UserModel[]>('http://localhost:8080/users', {params:queryParams})
  }

  changeUserPassword(changePasswordModel: ChangePasswordModel, userId: number) : Observable<MessageModel>{
    return this.http.put<MessageModel>('http://localhost:8080/users/' + userId, changePasswordModel)
  }

  deleteUser(userId: number) : Observable<any> {
    return this.http.delete<any>('http://localhost:8080/users/' + userId);
  }
}
