import { Component } from '@angular/core';
import {AuthClient} from "./core/client/auth.client";
import {UserClient} from "./core/client/user.client";
import {TokenStorageService} from "./core/service/token-storage.service";
import {PatientClient} from "./core/client/patient.client";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'patientRecordsUi';


  constructor(private authClient: AuthClient,
              private userClient: UserClient,
              private patientClient: PatientClient,
              private tokenStorageService: TokenStorageService) {
  }
}
