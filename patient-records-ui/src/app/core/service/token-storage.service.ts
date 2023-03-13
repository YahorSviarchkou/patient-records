import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class TokenStorageService {

  constructor() {
  }

  public saveToken(token: string) {
    localStorage.setItem("token", token)
  }

  public getToken(): string {
    return localStorage.getItem("token") != null
      ? localStorage.getItem("token")!
      : ""
  }
}
