import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AuthGuard } from './guards/auth-guard.service';

const routes: Routes = [
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  { path: 'home', loadChildren: './home/home.module#HomeModule' },
  {
    path: 'login',
    loadChildren: './login/login.module#LoginModule',
    data: {animation: 'HomePage'}
  },
  {
    path: 'register',
    loadChildren: './register/register.module#RegisterModule',
    data: {animation: 'AboutPage'}
  },
  {
    path: 'search',
    loadChildren: './search/search.module#SearchModule'
  },
  {
    path: 'advancedSearch',
    loadChildren: './search/advanced-search/advanced-search.module#AdvancedSearchModule'
  },
  {
    path: 'registerConfirm/:auth',
    loadChildren:
      './user/register-confirm/register-confirm.module#RegisterConfirmModule'
  },
  {
    path: 'newsletter/:type/:token',
    loadChildren:
      './newsletter-auth/newsletter-auth.module#NewsletterAuthModule'
  },
  {
    path: 'companyProfile/:id',
    loadChildren:
      './commonComponents/company-profile/company-profile.module#CompanyProfileModule'
  },
  {
    path: 'branchProfile/:idCompany/:idBranch',
    loadChildren:
      './commonComponents/branch-profile/branch-profile.module#BranchProfileModule'
  },
  {
    path: 'branchProfile/:idBranch',
    loadChildren:
      './commonComponents/branch-profile/branch-profile.module#BranchProfileModule'
  },
  {
    path: 'user/personalData',
    loadChildren:
      './user/personal-data/personal-data.module#PersonalDataModule',
    canActivate: [AuthGuard]
  },
  {
    path: 'user/accountData',
    loadChildren: './user/account-data/account-data.module#AccountDataModule',
    canActivate: [AuthGuard]
  },
  {
    path: 'user/companyData',
    loadChildren: './user/company/company.module#CompanyModule',
    canActivate: [AuthGuard]
  },
  { path: '**', redirectTo: 'home', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
