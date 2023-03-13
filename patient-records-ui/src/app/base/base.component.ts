import {Component, OnInit} from '@angular/core';
import {UserClient} from "../core/client/user.client";
import {UserModel} from "../core/model/user.model";
import {LoginService} from "../core/service/login.service";
import {NgForm} from "@angular/forms";
import {Router} from "@angular/router";
import {ChangePasswordModel} from "../core/model/change-password.model";

@Component({
  selector: 'app-base',
  templateUrl: './base.component.html',
  styleUrls: ['./base.component.scss']
})
export class BaseComponent implements OnInit {

  activeUser: UserModel = this.loginService.getActiveUser()
  switcher: boolean = true
  adminChangeMode: boolean = false

  login!: string
  fio!: string
  age!: number
  minBirthday!: Date
  maxBirthday!: Date
  minCreation!: Date
  maxCreation!: Date

  constructor(private userClient: UserClient,
              private loginService: LoginService,
              private router: Router) { }

  ngOnInit(): void {
  }

  setFilters(form: NgForm){
    this.login = form.value.loginForm ? form.value.loginForm : ""
    this.fio = form.value.fioForm ? form.value.fioForm : ""
    this.age = form.value.ageForm ? form.value.ageForm : ""
    this.minBirthday = form.value.minBirthdayForm != "" ? new Date(form.value.minBirthdayForm) : form.value.minBirthdayForm
    this.maxBirthday = form.value.maxBirthdayForm != "" ? new Date(form.value.maxBirthdayForm) : form.value.maxBirthdayForm
    this.minCreation = form.value.minCreationForm != "" ? new Date(form.value.minCreationForm) : form.value.minCreationForm
    this.maxCreation = form.value.maxCreationForm != "" ? new Date(form.value.maxCreationForm) : form.value.maxCreationForm
  }

  doSwitch() {
    this.switcher = !this.switcher
  }

  doCreate() {
    let creationType
    if(this.activeUser.role.title === "ADMIN") {
      creationType = this.switcher ? "USER" : "DOCTOR"
    } else {
      creationType = "PATIENT"
    }
    this.router.navigate(['./workbench'], {queryParams: {type: creationType}})
  }

  adminModeSwitcher() {
    this.adminChangeMode = !this.adminChangeMode
    this.popup()
  }

  changeAdmin(form: NgForm){
    if(!(form.value.newPassword && form.value.oldPassword)) {
      alert("Поля не должны быть пустыми")
      return
    }
    let changePassword: ChangePasswordModel = {newPassword:form.value.newPassword, oldPassword:form.value.oldPassword}

    this.userClient.changeUserPassword(changePassword, this.activeUser.id!).subscribe({
      next: resp => {
        alert(resp.value)
        this.adminModeSwitcher()
      },
      error: err => {
        alert(err.error.value)
      }
    })
  }

  logout() {
    this.loginService.logout()
    this.router.navigate(['./auth'])
  }

  popup() {
    let popupBg = document.querySelector('.popup__bg')
    let popup = document.querySelector('.popUp')

    if(this.adminChangeMode) {
      popupBg?.classList.add('active')
      popup?.classList.add('active')
    } else {
      popupBg?.classList.remove('active')
      popup?.classList.remove('active')
    }

    document.addEventListener('click', (e) => {
      if(e.target === popupBg) {
        popupBg?.classList.remove('active')
        popup?.classList.remove('active')
        this.adminModeSwitcher()
      }
    })
  }
}
