import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {CommentModel} from "../../core/model/comment.model";
import {UtilsService} from "../../core/service/utils.service";
import {RoleService} from "../../core/service/role.service";
import {LoginService} from "../../core/service/login.service";
import {CommentClient} from "../../core/client/comment.client";
import {NgForm} from "@angular/forms";
import {PatientClient} from "../../core/client/patient.client";
import {PageService} from "../../core/service/page.service";
import {UserModel} from "../../core/model/user.model";

@Component({
  selector: 'app-comment',
  templateUrl: './comment.component.html',
  styleUrls: ['./comment.component.scss']
})
export class CommentComponent implements OnInit {

  @Input() patientId!: number
  activeUser = this.loginService.getActiveUser()

  comments: CommentModel[] = []
  commentsPage: CommentModel[] = []

  pageService = new PageService(this.comments)

  @Input() isWriting!: boolean
  @Output() isWritingChange = new EventEmitter<boolean>();

  constructor(private loginService: LoginService,
              private patientClient: PatientClient,
              private commentClient: CommentClient) { }

  ngOnInit(): void {
    this.loadComments()
  }

  ngOnChanges() {
    this.popup()
  }

  writeComment(form: NgForm) {
    if(!form.value.comment){
      return
    }

    let comment: CommentModel = {content:form.value.comment}
    this.patientClient.writeComment(comment, this.patientId, this.activeUser.id!).subscribe(resp => {
      this.isWritingChange.emit(this.isWriting = !this.isWriting)
      this.loadComments()
    })
  }

  loadComments() {
    if(this.activeUser.role.title === RoleService.CHIEF_PHYSICIAN_ROLE.title) {
      this.commentClient.getCommentsByPatientId(this.patientId).subscribe(resp => {
        this.comments = resp

        this.pageService = PageService.customPage(this.comments, 3)
        this.commentsPage = this.pageService.arrayPage
      })
    } else {
      this.commentClient.getCommentsByPatientAndUserId(this.patientId, this.activeUser.id!).subscribe(resp => {
        this.comments = resp

        this.pageService = PageService.customPage(this.comments, 3)
        this.commentsPage = this.pageService.arrayPage
      })
    }
  }

  formatDate(date: any) {
    return UtilsService.formatDate(date)
  }

  openPrevPage(){
    this.pageService.openPrevPage()
    this.commentsPage = this.pageService.arrayPage
  }

  openNextPage(){
    this.pageService.openNextPage()
    this.commentsPage = this.pageService.arrayPage
  }

  popup() {
    let popupBg = document.querySelector('.popup__bg')
    let popup = document.querySelector('.popUp')

    if(this.isWriting) {
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
        if(this.isWriting) {
          this.isWriting = !this.isWriting
          this.isWritingChange.emit(this.isWriting)
        }
      }
    })
  }
}
