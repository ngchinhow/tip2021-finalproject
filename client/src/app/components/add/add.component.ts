import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AddEventRequest } from 'src/app/dto/addeventrequest';
import { CalendarService } from 'src/app/services/calendar.service';

@Component({
  selector: 'app-add',
  templateUrl: './add.component.html',
  styleUrls: ['./add.component.css']
})
export class AddComponent implements OnInit {
  form!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private calendarService: CalendarService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.form = this.fb.group({
      eventAsString: this.fb.control('', [Validators.required, Validators.minLength(8)])
    })
  }

  addEvent() {
    this.calendarService.addEvent(this.form.value as AddEventRequest);
    this.router.navigate(['/month']);
  }

}
