import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from "rxjs";
import {RegisterModel} from "../model/register.model";
import {LoginModel} from "../model/login.model";
import {TokenModel} from "../model/token.model";
import {UserModel} from "../model/user.model";

@Injectable({
  providedIn: 'root',
})
export class AuthClient {

  constructor(private http: HttpClient) {
  }

  register(registerModel: RegisterModel): Observable<number> {
    return this.http.post<number>('http://localhost:8080/auth/register', registerModel)
  }

  login(loginModel: LoginModel): Observable<TokenModel> {
    return this.http.post<TokenModel>('http://localhost:8080/auth/login', loginModel)
  }

  getUserByToken(token: string) : Observable<UserModel> {
    let queryParams = {"accessToken":token}
    return this.http.get<UserModel>('http://localhost:8080/auth/user', {params:queryParams})
  }
}
