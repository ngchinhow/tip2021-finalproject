import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DayComponent } from './components/day.component';
import { MainComponent } from './components/main.component';
import { MonthComponent } from './components/month.component';

const routes: Routes = [
  { path: '', component: MainComponent },
  { path: 'month', component: MonthComponent },
  { path: 'day', component: DayComponent },
  { path: '**', redirectTo: '/', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
