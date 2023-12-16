import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { Page404Component } from './authentication/page404/page404.component';
import { AuthGuard } from './core/guard/auth.guard';
import { AuthLayoutComponent } from './layout/app-layout/auth-layout/auth-layout.component';
import { MainLayoutComponent } from './layout/app-layout/main-layout/main-layout.component';

const routes: Routes = [
  {
    path: '',
    component: MainLayoutComponent,
    canActivate: [AuthGuard],
    children: [
      { path: '', redirectTo: '/authentication/signin', pathMatch: 'full' },
      {
        path: 'dashboard',
        loadChildren: () =>
          import('./dashboard/dashboard.module').then((m) => m.DashboardModule)
      },
      {
        path: 'metaData',
        loadChildren: () =>
          import('./meta-data/meta-data.module').then((m) => m.MetaDataModule)
      },
      {
        path: 'user-management',
        loadChildren: () =>
          import('./user-management/user-management.module').then((m) => m.UserManagementModule)
      },
      {
        path: 'lead-management',
        loadChildren: () =>
          import('./lead-management/lead-management.module').then((m) => m.LeadManagementModule)
      },
      {
        path: 'follow-up',
        loadChildren: () =>
          import('./follow-up/follow-up.module').then((m) => m.FollowUpModule)
      },
      {
        path: 'project-management',
        loadChildren: () =>
          import('./project-management/project-management.module').then((m) => m.ProjectManagementModule)
      },
      {
        path: 'team-management',
        loadChildren: () =>
          import('./team-management/team-management.module').then((m) => m.TeamManagementModule)
      },
      {
        path: 'profile',
        loadChildren: () =>
          import('./profile/profile.module').then((m) => m.ProfileModule)
      },
      {
        path: 'booking-management',
        loadChildren: () =>
          import('./booking/booking.module').then((m) => m.BookingModule)
      },
      {
        path: 'targets',
        loadChildren: () =>
          import('./targets/targets.module').then((m) => m.TargetsModule)
      },
      {
        path: 'vacation-management',
        loadChildren: () =>
          import('./vacation-management/vacation-management.module').then((m) => m.VacationManagementModule)
      },
      {
        path: 'raw-lead-management',
        loadChildren: () =>
          import('./raw-lead-management/raw-lead-management.module').then((m) => m.RawLeadManagementModule)
      },
      /* {
        path: 'extra-pages',
        loadChildren: () =>
          import('./extra-pages/extra-pages.module').then(
            (m) => m.ExtraPagesModule
          )
      } */
    ]
  },
  {
    path: 'authentication',
    component: AuthLayoutComponent,
    loadChildren: () =>
      import('./authentication/authentication.module').then(
        (m) => m.AuthenticationModule
      )
  },
  { path: '**', component: Page404Component }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {})],
  exports: [RouterModule]
})
export class AppRoutingModule { }
