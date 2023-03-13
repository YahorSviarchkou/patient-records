import {Component, Input, OnInit, SimpleChange} from '@angular/core';
import {UserModel} from "../../core/model/user.model";
import {UserClient} from "../../core/client/user.client";
import {LoginService} from "../../core/service/login.service";
import {PageService} from "../../core/service/page.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-user-base',
  templateUrl: './user-base.component.html',
  styleUrls: ['./user-base.component.scss']
})
export class UserBaseComponent implements OnInit {

  @Input() login!: string

  users: UserModel[] = []
  usersPage: UserModel[] = []

  pageService = new PageService(this.users)

  constructor(private userClient: UserClient,
              private loginService: LoginService,
              private router: Router) { }

  ngOnInit(): void {
    this.setUsers()
  }

  ngOnChanges(changes: { [propName: string]: SimpleChange }) {
    this.login = changes['login'] ? changes['login'].currentValue : ""
    this.setUsers()
  }

  setUsers() {
    this.userClient.getUsers().subscribe(resp => {
      if(this.login && this.login !== "") {
        resp = resp.filter(u => u.login.includes(this.login))
      }
      this.users = resp.filter(u => u.id != this.loginService.getActiveUser().id)

      this.pageService = PageService.customPage(this.users, 10)
      this.usersPage = this.pageService.arrayPage
    })
  }

  openPrevPage(){
    this.pageService.openPrevPage()
    this.usersPage = this.pageService.arrayPage
  }

  openNextPage(){
    this.pageService.openNextPage()
    this.usersPage = this.pageService.arrayPage
  }

  toProfile(user: UserModel) {
    this.router.navigate(['./profile'], {queryParams: {id: user.id, type: "USER"}})
  }
}
