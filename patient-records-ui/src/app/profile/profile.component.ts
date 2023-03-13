import {Component, OnInit} from '@angular/core';
import {LoginService} from "../core/service/login.service";
import {ActivatedRoute, Router} from "@angular/router";
import {UserClient} from "../core/client/user.client";
import {DoctorClient} from "../core/client/doctor.client";
import {UserModel} from "../core/model/user.model";
import {DoctorModel} from "../core/model/doctor.model";
import {NgForm} from "@angular/forms";
import {UtilsService} from "../core/service/utils.service";
import {ChangePasswordModel} from "../core/model/change-password.model";
import {EMPTY, Observable} from "rxjs";
import {PositionModel} from "../core/model/position.model";


@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {

  DOCTOR: string = "DOCTOR"
  USER: string = "USER"

  id!: number
  type!: string
  user!: UserModel
  doctor!: DoctorModel
  editMode: boolean = false

  constructor(private loginService: LoginService,
              private userClient: UserClient,
              private doctorClient: DoctorClient,
              private activatedRoute: ActivatedRoute,
              private router: Router) {
  }

  ngOnInit(): void {
    this.activatedRoute.queryParams.subscribe(params => {
      this.id = params['id']
      this.type = params['type']

      if(this.type === this.USER){
        this.userClient.getUserById(this.id).subscribe(resp => {
          this.user = resp ? resp[0] : this.user
        })
      } else if (this.type === this.DOCTOR) {
        this.doctorClient.getDoctorById(this.id).subscribe(resp => {
          this.doctor = resp
        })
      }
    })
  }

  doEdit() {
    this.editMode = !this.editMode
  }

  doDelete() {
    let id = this.type === this.DOCTOR ? this.doctor.user.id : this.id
    this.userClient.deleteUser(id!).subscribe(resp =>{
      this.router.navigate(['./'])
    })
  }

  doSave(form: NgForm) {
    this.changePassword(form).subscribe({
      next: resp => {
        alert(resp.value)
      },
      error: err => {
        alert(err.error.value)
      }
    })
    if (this.type === this.DOCTOR && (form.value.fio || form.value.position)) {
      if(form.value.fio) {
        this.doctor.fio = form.value.fio
      }
      if(form.value.position) {
        this.doctor.position = {title: form.value.position}
      }
      this.doctorClient.updateDoctor(this.doctor).subscribe(resp => {
        this.doctor = resp
      })
    }
    this.doEdit()
  }

  changePassword(form: NgForm) : Observable<any>{
    if(form.value.oldPassword && form.value.newPassword) {
      let id = this.type === this.DOCTOR ? this.doctor.user.id : this.id
      let changePass: ChangePasswordModel = {oldPassword:form.value.oldPassword, newPassword:form.value.newPassword}

      return this.userClient.changeUserPassword(changePass, id!)
    }
    return EMPTY
  }

  goBack() {
    this.router.navigate(['./'])
  }

  formatDate(dateAny: any) : string{
    return UtilsService.formatDate(dateAny)
  }
}
