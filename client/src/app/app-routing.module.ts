import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DayComponent } from './components/day/day.component';
import { LoginComponent } from './components/login/login.component';
import { MainComponent } from './components/main/main.component';
import { MonthComponent } from './components/month/month.component';
import { ProvidersResolver } from './resolvers/providers.resolver';
import { AuthGuard } from './guards/auth.guard';
import { MonthResolver } from './resolvers/month.resolver';
import { DayResolver } from './resolvers/day.resolver';
import { AddComponent } from './components/add/add.component';

const routes: Routes = [
  {
    path: '',
    component: MainComponent,
    canActivate: [AuthGuard],
    children: [
      { path: '', redirectTo: 'month', pathMatch: 'full' },
      { path: 'month', component: MonthComponent },
      {
        path: 'month/:year/:month',
        component: MonthComponent,
        resolve: { events: MonthResolver }
      },
      {
        path: 'day/:year/:month/:day',
        component: DayComponent,
        resolve: { events: DayResolver }
      },
      { path: 'add', component: AddComponent }
    ]
  },
  { path: 'login', component: LoginComponent, resolve: { providers: ProvidersResolver } },
  { path: '**', redirectTo: '/', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
