import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { Routes, RouterModule } from '@angular/router';
import { FormsModule } from "@angular/forms";
import { HTTP_INTERCEPTORS, HttpClientModule } from "@angular/common/http";
import { AuthInterceptor } from "./core/helper/auth-interceptor";
import { AuthorizationComponent } from './authorization/authorization.component';
import { ProfileComponent } from './profile/profile.component';
import { BaseComponent } from './base/base.component';
import { UserBaseComponent } from './base/user-base/user-base.component';
import { PatientBaseComponent } from './base/patient-base/patient-base.component';
import { DoctorBaseComponent } from './base/doctor-base/doctor-base.component';
import { SpinnerComponent } from './spinner/spinner.component';
import { PatientCardComponent } from './patient-card/patient-card.component';
import { WorkbenchComponent } from './workbench/workbench.component';
import { CommentComponent } from './patient-card/comment/comment.component';
import {AuthGuard} from "./core/guard/auth.guard";

const appRoutes: Routes =[
  { path: '', component: BaseComponent, canActivate: [AuthGuard],
    data: {
      role: ['ADMIN', 'LOCAL_ADMIN', "CHIEF_PHYSICIAN", "PHYSICIAN"]
    }
  },
  { path: 'profile', component: ProfileComponent, canActivate: [AuthGuard],
    data: {
      role: ['ADMIN']
    }
  },
  { path: 'patient-card', component: PatientCardComponent, canActivate: [AuthGuard],
    data: {
      role: ['LOCAL_ADMIN', "CHIEF_PHYSICIAN", "PHYSICIAN"]
    }
  },
  { path: 'workbench', component: WorkbenchComponent, canActivate: [AuthGuard],
    data: {
      role: ['ADMIN', 'LOCAL_ADMIN']
    }
  },
  { path: 'auth', component: AuthorizationComponent},
  { path: '**', redirectTo: '/'}
];

@NgModule({
  declarations: [
    AppComponent,
    AuthorizationComponent,
    ProfileComponent,
    BaseComponent,
    UserBaseComponent,
    PatientBaseComponent,
    DoctorBaseComponent,
    SpinnerComponent,
    PatientCardComponent,
    WorkbenchComponent,
    CommentComponent
  ],
  providers:[
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true,
    },
  ],
    imports: [
        BrowserModule,
        RouterModule.forRoot(appRoutes),
        HttpClientModule,
        FormsModule,
    ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
