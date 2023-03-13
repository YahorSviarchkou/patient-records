import {Component, Input, OnInit, SimpleChange} from '@angular/core';
import {UserModel} from "../../core/model/user.model";
import {UserClient} from "../../core/client/user.client";
import {LoginService} from "../../core/service/login.service";
import {DoctorModel} from "../../core/model/doctor.model";
import {DoctorClient} from "../../core/client/doctor.client";
import {PatientModel} from "../../core/model/patient.model";
import {finalize} from "rxjs";
import {PageService} from "../../core/service/page.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-doctor-base',
  templateUrl: './doctor-base.component.html',
  styleUrls: ['./doctor-base.component.scss']
})
export class DoctorBaseComponent implements OnInit {

  @Input() login!: string
  @Input() fio!: string

  doctors: DoctorModel[] = []
  doctorsPage: DoctorModel[] = []

  pageService = new PageService(this.doctors)

  constructor(private doctorClient: DoctorClient,
              private router: Router) { }

  ngOnInit(): void {
    this.setDoctors()
  }

  ngOnChanges(changes: { [propName: string]: SimpleChange }) {
    this.login = changes['login'] ? changes['login'].currentValue : ""
    this.fio = changes['fio'] ? changes['fio'].currentValue : ""
    this.setDoctors()
  }

  setDoctors() {
    this.doctorClient.getDoctors().subscribe(resp => {
      if(this.fio && this.fio !== "") {
        resp = resp.filter(doctor => doctor.fio.includes(this.fio))
      }
      if(this.login && this.login !== "") {
        resp = resp.filter(doctor => doctor.user.login.includes(this.login))
      }
      this.doctors = resp
      this.pageService = PageService.customPage(this.doctors, 10)
      this.doctorsPage = this.pageService.arrayPage
    })
  }

  openPrevPage(){
    this.pageService.openPrevPage()
    this.doctorsPage = this.pageService.arrayPage
  }

  openNextPage(){
    this.pageService.openNextPage()
    this.doctorsPage = this.pageService.arrayPage
  }

  toProfile(doctor: DoctorModel) {
    this.router.navigate(['./profile'], {queryParams: {id: doctor.id, type: "DOCTOR"}})
  }
}
