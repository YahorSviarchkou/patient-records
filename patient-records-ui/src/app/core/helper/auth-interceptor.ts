import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {delay, finalize, Observable} from 'rxjs';
import {TokenStorageService} from "../service/token-storage.service";
import {LoaderService} from "../service/loader.service";

@Injectable({
  providedIn: 'root'
})
export class AuthInterceptor implements HttpInterceptor {

  private totalRequests = 0;

  constructor(private tokenStorageService: TokenStorageService,
              private loaderService: LoaderService) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = this.tokenStorageService.getToken();

    this.totalRequests++;
    this.loaderService.setLoading(true);

    if (req.url.includes("/auth/login") || !token) {
      return next.handle(req).pipe(
        finalize(() => {
          this.totalRequests--;
          if (this.totalRequests == 0) {
            this.loaderService.setLoading(false);
          }
        })
      );
    }

    const req1 = req.clone({
      headers: req.headers.set('Authorization', `Bearer ${token}`),
    });
    return next.handle(req1).pipe(
      finalize(() => {
        this.totalRequests--;
        if (this.totalRequests == 0) {
          this.loaderService.setLoading(false);
        }
      })
    );
  }
}
