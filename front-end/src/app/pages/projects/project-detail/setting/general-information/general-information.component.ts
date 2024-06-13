import {Component, Injector, OnInit} from '@angular/core';
import {BaseComponent} from "../../../../../share/ui/base-component/base.component";
import {ProjectService} from "../../../../../service/project.service";
import {FormGroup, Validators} from "@angular/forms";
import {ProjectStoreService} from "../../../project-store.service";
import {Project} from "../../../../../models/project.model";

@Component({
  selector: 'app-general-information',
  templateUrl: './general-information.component.html',
  styleUrl: './general-information.component.scss'
})
export class GeneralInformationComponent extends BaseComponent implements OnInit {
  listProject: any[] = []
  projectId: any;

  constructor(injector: Injector,
              private projectService: ProjectService
  ) {
    super(injector);
    this.projectId = this.projectStoreService.id;
  }

  ngOnInit() {
    this.buildForm()
    this.getProject(this.projectId);
  }

  buildForm() {
    this.form = this.fb.group({
      name: ['', Validators.required],
      description: [''],
      warningTime: [''],
      dangerTime: [''],
    })
  }

  getProject(projectId: string) {
    this.projectService.getProject(projectId).subscribe({
      next: (res: any) => {
        console.log('res:', res);
        this.form.patchValue(res.data);
      }, error: (err: any) => {
        this.createErrorToast("Lỗi", "Không tìm thấy dự án");
      }
    })
  }

  handleSave(form: FormGroup) {
    const data: Project = {
      ...form.value
    }
    this.projectService.updateProject(this.projectId, data).subscribe({
      next: (res: any) => {
        this.createSuccessToast("Thành công", "Cập nhật thông tin dự án thành công");
      }, error: (err: any) => {
        this.createErrorToast("Lỗi", "Cập nhật thông tin dự án thất bại");
      }
    })
  }
}
