import {Component, Injector, OnInit} from '@angular/core';
import {ProjectService} from "../../../service/project.service";
import {BaseComponent} from "../../../share/ui/base-component/base.component";
import {FormGroup, Validators} from "@angular/forms";
import {Project} from "../../../models/project.model";

@Component({
  selector: 'app-project-create',
  templateUrl: './project-create.component.html',
  styleUrl: './project-create.component.scss'
})
export class ProjectCreateComponent extends BaseComponent implements OnInit {
  listProject: any[] = []

  constructor(injector: Injector,
              private projectService: ProjectService
  ) {
    super(injector);
  }

  async ngOnInit() {
    await this.getProjects();
    this.changeStructureListProject();
    this.buildForm();
  }

  async getProjects() {
    try {
      const res: any = await this.projectService.getProjects(this.user.id, {}).toPromise();
      console.log('res:', res);
      this.listProject = res.data;
    } catch (err: any) {
      this.createErrorToast('Lỗi', err.message);
    }
  }

  buildForm() {
    this.form = this.fb.group({
      name: ['', Validators.required],
      description: [''],
      parentId: [''],
    })
  }

  changeStructureListProject() {
    this.listProject = this.listProject.map((item: any) => {
      return {
        label: item.data.name,
        key: item.data.id,
        children: this.setChildProject(item)
      }
    })
  }

  setChildProject(item: any): any {
    console.log(item.children)
    let children: any[] = []
    if (item.children.length !== 0) {
      item.children.forEach((child: any) => {
        children.push({
          label: child.data.name,
          key: child.data.id,
          children: this.setChildProject(child)
        })
      })
    }
    return children;
  }

  handleSave(form: FormGroup) {
    const data: Project = {
      name: form.value.name,
      description: form.value.description,
      parentId: form.value.parentId.key == undefined ? null : form.value.parentId.key
    }
    this.projectService.createProject(data).subscribe(
      {
        next: (res: any) => {
          this.createSuccessToast('Thành công', 'Thêm mới dự án thành công');
          this.router.navigate(['/projects']).then();
        },
        error: (err: any) => {
          this.createErrorToast('Error', err.message);
        }
      }
    )
  }
}
