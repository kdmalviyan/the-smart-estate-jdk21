import { HashLocationStrategy, LocationStrategy } from '@angular/common';
import {
  HttpClient, HttpClientModule,
  HTTP_INTERCEPTORS
} from '@angular/common/http';
import { NgModule } from '@angular/core';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { TranslateLoader, TranslateModule } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { ClickOutsideModule } from 'ng-click-outside';
import {
  PerfectScrollbarConfigInterface, PerfectScrollbarModule,
  PERFECT_SCROLLBAR_CONFIG
} from 'ngx-perfect-scrollbar';
import { NgxSpinnerModule } from 'ngx-spinner';
import { NgxUiLoaderModule } from 'ngx-ui-loader';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { CoreModule } from './core/core.module';
import { JwtInterceptor } from './core/interceptor/jwt.interceptor';
import { StorageService } from './core/service/storage.service';
import { WINDOW_PROVIDERS } from './core/service/window.service';
import { AuthLayoutComponent } from './layout/app-layout/auth-layout/auth-layout.component';
import { MainLayoutComponent } from './layout/app-layout/main-layout/main-layout.component';
import { HeaderComponent } from './layout/header/header.component';
import { PageLoaderComponent } from './layout/page-loader/page-loader.component';
import { RightSidebarComponent } from './layout/right-sidebar/right-sidebar.component';
import { SidebarComponent } from './layout/sidebar/sidebar.component';
import { SharedModule } from './shared/shared.module';


const DEFAULT_PERFECT_SCROLLBAR_CONFIG: PerfectScrollbarConfigInterface = {
  suppressScrollX: true,
  wheelPropagation: false
};

export function createTranslateLoader(http: HttpClient): any {
  return new TranslateHttpLoader(http, 'assets/i18n/', '.json');
}

@NgModule({
    declarations: [
        AppComponent,
        HeaderComponent,
        PageLoaderComponent,
        SidebarComponent,
        RightSidebarComponent,
        AuthLayoutComponent,
        MainLayoutComponent
    ],
    imports: [
        BrowserModule,
        BrowserAnimationsModule,
        AppRoutingModule,
        HttpClientModule,
        PerfectScrollbarModule,
        NgxSpinnerModule,
        MatSnackBarModule,
        ClickOutsideModule,
        TranslateModule.forRoot({
            loader: {
                provide: TranslateLoader,
                useFactory: createTranslateLoader,
                deps: [HttpClient]
            }
        }),
        CoreModule,
        SharedModule,
        NgxUiLoaderModule.forRoot({
            "bgsColor": "red",
            "bgsOpacity": 0.5,
            "bgsPosition": "bottom-right",
            "bgsSize": 60,
            "bgsType": "ball-spin-clockwise",
            "blur": 7,
            "delay": 0,
            "fastFadeOut": true,
            "fgsColor": "#FFA500 ",
            "fgsPosition": "center-center",
            "fgsSize": 60,
            "fgsType": "fading-circle",
            "gap": 10,
            "logoPosition": "center-center",
            "logoSize": 70,
            "logoUrl": "",
            "masterLoaderId": "master",
            "overlayBorderRadius": "0",
            "overlayColor": "rgba(40,40,40,0.81)",
            "pbColor": "#FFA500",
            "pbDirection": "ltr",
            "pbThickness": 6,
            "hasProgressBar": true,
            "text": "",
            "textColor": "#FFFFFF",
            "textPosition": "center-center",
            "maxTime": -1,
            "minTime": 300
        })
    ],
    providers: [
        StorageService,
        { provide: LocationStrategy, useClass: HashLocationStrategy },
        {
            provide: PERFECT_SCROLLBAR_CONFIG,
            useValue: DEFAULT_PERFECT_SCROLLBAR_CONFIG
        },
        { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },
        WINDOW_PROVIDERS
    ],
    bootstrap: [AppComponent]
})
export class AppModule { }


/* { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true }, */