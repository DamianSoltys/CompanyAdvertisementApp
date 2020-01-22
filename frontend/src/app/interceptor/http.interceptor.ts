import { Injectable } from '@angular/core';
import {
  HttpInterceptor,
  HttpRequest,
  HttpResponse,
  HttpHandler,
  HttpEvent,
  HttpErrorResponse
} from '@angular/common/http';

import { Observable, throwError } from 'rxjs';
import { map, catchError, finalize } from 'rxjs/operators';
import { LoaderService } from '../services/loader.service';

@Injectable()
export class HttpConfigInterceptor implements HttpInterceptor {
  constructor(public loaderService: LoaderService) { }

  intercept(
    request: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    this.loaderService.showLoader();
    const token: string = localStorage.getItem('token');

    if (token) {
      request = request.clone({
        headers: request.headers.set('Authorization', 'Basic ' + token)
      });
    }
    // if (!request.headers.has('Content-Type')) {
    //   request = request.clone({
    //     headers: request.headers.set('Content-Type', 'application/json')
    //   });
    // }
    // request = request.clone({
    //   headers: request.headers.set('Accept', 'application/json')
    // });

    return next
      .handle(request)
      .pipe(finalize(() => this.loaderService.hideLoader()));
  }
}
