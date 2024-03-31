import {Component, Injector} from '@angular/core';
import { BaseComponent } from '../../../share/ui/base-component/base.component';
import {Validators} from "@angular/forms";
import {FileUploadEvent} from "primeng/fileupload";
import {DialogService} from "primeng/dynamicdialog";

@Component({
  selector: 'app-task-create',
  templateUrl: './task-create.component.html',
  styleUrl: './task-create.component.scss'
})
export class TaskCreateComponent extends BaseComponent {
  cities = [
    { name: 'New York', code: 'NY' },
    { name: 'Rome', code: 'RM' },
    { name: 'London', code: 'LDN' },
    { name: 'Istanbul', code: 'IST' },
    { name: 'Paris', code: 'PRS' }
  ];
  constructor(injector: Injector) {
    super(injector);
  }

  ngOnInit() {
    this.buildForm();
  }

  buildForm() {
    this.form = this.fb.group({
      subject: ['', Validators.required],
      reporter: ['', Validators.required],
    });
  }


  onUpload($event: FileUploadEvent) {

  }
}
