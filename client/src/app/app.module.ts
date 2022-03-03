import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { MonthComponent } from './components/month.component';
import { MainComponent } from './components/main.component';
import { DayComponent } from './components/day.component';
import { DateTimeService } from './services/datetime.service';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { MatGridListModule } from '@angular/material/grid-list';

@NgModule({
  declarations: [
    AppComponent,
    MonthComponent,
    MainComponent,
    DayComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatGridListModule
  ],
  providers: [DateTimeService],
  bootstrap: [AppComponent]
})
export class AppModule { }
