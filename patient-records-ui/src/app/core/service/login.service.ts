import {Injectable} from "@angular/core";
import {UserModel} from "../model/user.model";

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  getActiveUser() : UserModel {
    return localStorage.getItem("active") != null
      ? JSON.parse(localStorage.getItem("active")!)
      : null
  }

  setActiveUser(user : UserModel) {
    localStorage.setItem("active", JSON.stringify(user))
  }

  isActiveNow() : boolean{
    return localStorage.getItem("active") != null
  }

  logout() {
    localStorage.clear()
  }
}
