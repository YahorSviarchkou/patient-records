import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {PatientModel} from "../core/model/patient.model";
import {PatientClient} from "../core/client/patient.client";
import {CommentModel} from "../core/model/comment.model";
import {ImageClient} from "../core/client/image.client";
import {CommentClient} from "../core/client/comment.client";
import {LoginService} from "../core/service/login.service";
import {RoleService} from "../core/service/role.service";
import {DomSanitizer} from "@angular/platform-browser";
import {UtilsService} from "../core/service/utils.service";
import {NgForm} from "@angular/forms";

@Component({
  selector: 'app-patient-card',
  templateUrl: './patient-card.component.html',
  styleUrls: ['./patient-card.component.scss']
})
export class PatientCardComponent implements OnInit {

  activeUser = this.loginService.getActiveUser()

  id!: number
  patient!: PatientModel

  editMode = false
  isWriting = false

  photo: any = "../../assets/images/no-photo.png"
  selectedPhoto: File | undefined

  constructor(private loginService: LoginService,
              private patientClient: PatientClient,
              private imageClient: ImageClient,
              private commentClient: CommentClient,
              private activatedRoute: ActivatedRoute,
              private router: Router,
              private sanitizer: DomSanitizer) { }

  ngOnInit(): void {
    this.activatedRoute.queryParams.subscribe(params => {
      this.id = params['id']

      this.patientClient.getPatientById(this.id).subscribe(resp => {
        this.patient = resp
      })

      this.loadPhoto()
    })
  }

  doDelete() {
    this.patientClient.deletePatientById(this.id).subscribe(resp => {
      this.router.navigate(['./'])
    })
  }

  doEdit(form: NgForm) {
    if(form.value.fio || form.value.birthday) {
      if(form.value.fio) {
        this.patient.fio = form.value.fio
      }
      if(form.value.birthday != "") {
        this.patient.dateOfBirthday = new Date(form.value.birthday)
      }
      this.patientClient.updatePatient(this.patient).subscribe(resp => {
        this.patient = resp
      })
    }
    this.updatePhoto()
    this.switchEditMode()
  }

  doBack() {
    this.router.navigate(['./'])
  }

  switchEditMode() {
    this.editMode = !this.editMode
  }

  switchWriting() {
    this.isWriting = !this.isWriting
  }

  assignPhoto(e: any) {
    this.selectedPhoto = e.target.files[0];
  }

  updatePhoto() {
    if(!this.selectedPhoto) return

    let formData = new FormData();
    formData.set('file', this.selectedPhoto!)

    this.imageClient.uploadFile(formData, this.patient.id!).subscribe(resp =>{
      this.ngOnInit()
    })
  }

  loadPhoto() {
    this.imageClient.getFile(this.id).subscribe(response => {
      let blob: Blob = response.body as Blob
      if(blob !== null) {
        let urlCreator = window.URL || window.webkitURL
        let imageUrl = urlCreator.createObjectURL(blob)
        this.photo = this.sanitizer.bypassSecurityTrustUrl(imageUrl)
      }
    })
  }

  formatDate(date: any) {
    return UtilsService.formatDate(date)
  }
}
