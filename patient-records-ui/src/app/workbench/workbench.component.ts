import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {NgForm} from "@angular/forms";
import {RoleModel} from "../core/model/role.model";
import {RoleService} from "../core/service/role.service";
import {UserClient} from "../core/client/user.client";
import {DoctorClient} from "../core/client/doctor.client";
import {EMPTY, forkJoin, lastValueFrom, map, mergeMap, Observable} from "rxjs";
import {UserModel} from "../core/model/user.model";
import {AuthClient} from "../core/client/auth.client";
import {RegisterModel} from "../core/model/register.model";
import {DoctorModel} from "../core/model/doctor.model";
import {PatientModel} from "../core/model/patient.model";
import {PatientClient} from "../core/client/patient.client";
import {ImageClient} from "../core/client/image.client";
import {DoctorRequestModel} from "../core/model/doctor-request.model";

@Component({
  selector: 'app-workbench',
  templateUrl: './workbench.component.html',
  styleUrls: ['./workbench.component.scss']
})
export class WorkbenchComponent implements OnInit {

  type!: string

  roles: RoleModel[] = []
  role: RoleModel | undefined
  selectedRole: RoleModel | undefined

  selectedPhoto: File | undefined

  constructor(private authClient: AuthClient,
              private userClient: UserClient,
              private doctorClient: DoctorClient,
              private patientClient: PatientClient,
              private imageClient: ImageClient,
              private activatedRoute: ActivatedRoute,
              private router: Router) { }

  ngOnInit(): void {
    this.activatedRoute.queryParams.subscribe(params => {
      this.type = params['type']

      this.initRoles()
    })
  }

  doCreate(form: NgForm) {
    if(this.type === "USER") {
     this.createUser(form).subscribe(r => this.router.navigate(['./']))
    }
    if(this.type === "DOCTOR") {
      this.createDoctor(form).subscribe(r => this.router.navigate(['./']))
    }
    if(this.type === "PATIENT") {
      this.createPatientAndAssignPhoto(form)
    }
  }

  doBack() {
    this.router.navigate(['./'])
  }

  createUser(form: NgForm) : Observable<number>{
    if(!(form.value.login && form.value.password && this.role && this.role.title)) {
      return EMPTY
    }
    let user: RegisterModel = {login:form.value.login, password:form.value.password, roleTitle:this.role!.title}
    return this.authClient.register(user)
  }

  createDoctor(form: NgForm) : Observable<DoctorModel>{
    if(!(form.value.login && form.value.password && this.role && this.role.title)) {
      return EMPTY
    }
    if(!(form.value.fio && form.value.position)) {
      return EMPTY
    }

    let doctorRequest: DoctorRequestModel = {
      fio:form.value.fio,
      position:form.value.position,
      login:form.value.login,
      password:form.value.password,
      roleTitle:this.role!.title
    }

    return this.doctorClient.createDoctor(doctorRequest)
  }

  createPatient(form: NgForm) : Observable<PatientModel> {
    if(!(form.value.fio && form.value.birthday)) {
      return EMPTY
    }
    let patient: PatientModel = {fio:form.value.fio, dateOfBirthday:new Date(form.value.birthday)}
    return this.patientClient.createPatient(patient)
  }

  createPatientAndAssignPhoto(form: NgForm){
    this.createPatient(form).subscribe(patient => {
      if(!(this.selectedPhoto && patient)) {
        return
      }

      let formData = new FormData();
      formData.set('file', this.selectedPhoto!)

      this.imageClient.uploadFile(formData, patient.id!).subscribe(resp =>{
        this.router.navigate(['./'])
      })
    })
  }

  assignRole(e: any): void {
    this.role = this.roles.find(r => r?.title === e.target.value);
  }

  assignPhoto(e: any): void {
    this.selectedPhoto = e.target.files[0];
  }

  initRoles() {
    if(this.type === "USER") {
      this.roles = RoleService.ROLES.filter(r => r.title.includes("ADMIN"))
    }
    if(this.type === "DOCTOR") {
      this.roles = RoleService.ROLES.filter(r => !r.title.includes("ADMIN"))
    }
  }
}
