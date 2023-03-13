import {Component, Input, OnInit, SimpleChange} from '@angular/core';
import {PatientModel} from "../../core/model/patient.model";
import {PatientClient} from "../../core/client/patient.client";
import {PageService} from "../../core/service/page.service";
import {Router} from "@angular/router";
import {stringify} from "@angular/compiler/src/util";
import {UtilsService} from "../../core/service/utils.service";

@Component({
  selector: 'app-patient-base',
  templateUrl: './patient-base.component.html',
  styleUrls: ['./patient-base.component.scss']
})
export class PatientBaseComponent implements OnInit {

  @Input() fio!: string
  @Input() age!: number
  @Input() minBirthday!: Date
  @Input() maxBirthday!: Date
  @Input() minCreation!: Date
  @Input() maxCreation!: Date

  patients: PatientModel[] = []
  patientsPage: PatientModel[] = []

  pageService = new PageService(this.patients)

  constructor(private patientClient: PatientClient,
              private router: Router) { }

  ngOnInit(): void {
    this.setPatients()
  }

  ngOnChanges(changes: { [propName: string]: SimpleChange }) {
    this.fio = changes['fio']
      ? changes['fio'].currentValue
      : ""
    this.age = changes['age']
      ? changes['age'].currentValue
      : ""
    this.minBirthday = changes['minBirthday']
      ? changes['minBirthday'].currentValue
      : ""
    this.maxBirthday = changes['maxBirthday']
      ? changes['maxBirthday'].currentValue
      : ""
    this.minCreation = changes['minCreation']
      ? changes['minCreation'].currentValue
      : ""
    this.maxCreation = changes['maxCreation']
      ? changes['maxCreation'].currentValue
      : ""
    this.setPatients()
  }

  setPatients() {
    this.patientClient.getAllPatients(this.fio ? this.fio : "", this.requestDate(this.minBirthday),  this.requestDate(this.maxBirthday),
      this.requestDate(this.minCreation),  this.requestDate(this.maxCreation))
      .subscribe(resp => {
        if(this.age) {
          resp = resp.filter(patient => patient.age! >= this.age)
        }
        this.patients = resp
        this.pageService = PageService.customPage(this.patients, 10)
        this.patientsPage = this.pageService.arrayPage
      })
  }

  openPrevPage(){
    this.pageService.openPrevPage()
    this.patientsPage = this.pageService.arrayPage
  }

  openNextPage(){
    this.pageService.openNextPage()
    this.patientsPage = this.pageService.arrayPage
  }

  toPatientCard(patient: PatientModel) {
    this.router.navigate(['./patient-card'], {queryParams: {id: patient.id}})
  }

  requestDate(date: Date) : string{
    if(date) {
      console.log(date.getMonth())
      return [this.digitToString(date.getFullYear()),
          this.digitToString(date.getMonth() + 1),
          this.digitToString(date.getDate())].join("-") + " "
        + [this.digitToString(date.getHours()), this.digitToString(date.getMinutes()), "00"].join(":")
    }
    return ""
  }

  formatDate(dateAny: any) : string{
    return UtilsService.formatDate(dateAny)
  }

  digitToString(value: number) {
    return UtilsService.digitToString(value)
  }
}
