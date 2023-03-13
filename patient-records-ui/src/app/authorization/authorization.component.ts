import { Component, OnInit } from '@angular/core';
import {NgForm} from "@angular/forms";
import {AuthClient} from "../core/client/auth.client";
import {LoginModel} from "../core/model/login.model";
import {TokenStorageService} from "../core/service/token-storage.service";
import {Router} from "@angular/router";
import {LoginService} from "../core/service/login.service";

@Component({
  selector: 'app-authorization',
  templateUrl: './authorization.component.html',
  styleUrls: ['./authorization.component.scss']
})
export class AuthorizationComponent implements OnInit {

  constructor(private authClient: AuthClient,
              private tokenStorageService: TokenStorageService,
              private loginService: LoginService,
              private router: Router) { }

  ngOnInit(): void {
  }

  onSubmit(myForm: NgForm) {
    let loginModel: LoginModel = {"login":myForm.value.login, "password":myForm.value.password}
    this.authClient.login(loginModel).subscribe({
      next: resp => {
        this.tokenStorageService.saveToken(resp.accessToken)

        this.authClient.getUserByToken(resp.accessToken).subscribe(resp => {
          this.loginService.setActiveUser(resp)
          this.router.navigate(['./'])
        })
      },
      error: () => {
        alert("Неверный логин или пароль")
      }
    })
  }
}
