import {Component, Injector, OnInit} from '@angular/core';
import {BaseComponent} from "../../share/ui/base-component/base.component";
import {PostService} from "../../service/post.service";
import {TreeNodeSelectEvent, TreeNodeUnSelectEvent} from "primeng/tree";
import {ProjectService} from "../../service/project.service";
import {Validators} from "@angular/forms";
import {FileSelectEvent} from "primeng/fileupload";
import {DocumentService} from "../../service/document.service";
import {Attachment} from "../../models/attachment.model";

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrl: './post.component.scss'
})
export class PostComponent extends BaseComponent implements OnInit {
  listProject: any[] = [];
  fileList: any[] = [];
  data2edit: any;
  visibleModalAddPost = false;
  dialogTitle: string = '';

  constructor(injector: Injector,
              private projectService: ProjectService,
              private postService: PostService,
              private documentService: DocumentService) {
    super(injector);
  }

  async ngOnInit() {
    this.buildForm();
    this.getPosts();
    await this.getProjects();
  }

  buildForm() {
    this.form = this.fb.group({
      title: [null, [Validators.required]],
      content: [null, [Validators.required]],
      projectId: [null, [Validators.required]],
    });
  }

  async getPosts(projectId: any = null) {
  try {
    const res: any = await this.postService.getPosts(projectId).toPromise();
    console.log("res:", res);
    this.listData = res.data;
  } catch (err) {
    // Handle error here
    console.error(err);
  }
}

  async getProjects() {
    try {
      const res: any = await this.projectService.getProjects(this.user.id, {}).toPromise();
      this.listProject = res.data;
      this.changeStructureListProject();
    } catch (err: any) {
      this.createErrorToast('Lỗi', err.message);
    }
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

  onSelectProject($event: TreeNodeSelectEvent) {
    this.getPosts($event.node.key)
  }

  onUnselectProject($event: TreeNodeUnSelectEvent) {
    this.getPosts();
  }

  showModalAddPost() {
    this.dialogTitle = 'Thêm mới bài viết';
    this.visibleModalAddPost = true;
  }

  onCloseAddModal() {
    this.dialogTitle = '';
    this.visibleModalAddPost = false;
  }

  showModalEditPost(id: any, projectName: any) {
    this.dialogTitle = 'Chỉnh sửa bài viết';
    this.postService.getPost(id).subscribe({
      next: res => {
        this.data2edit = res.data;
        const projectValue = {
          label: projectName,
          key: this.data2edit.projectId,
        }
        this.form.patchValue({
          title: res.data.title,
          content: res.data.content,
          projectId: projectValue
        });

        console.log(this.listData.find((item: any) => item.id == this.data2edit.id))
        if (this.listData.find((item: any) => item.id == this.data2edit.id).attachments.length > 0) {
          this.getAttachments2Edit(this.data2edit.id);
        } else {
          this.fileList = [];
        }
        this.visibleModalAddPost = true;
      }
    })
    this.visibleModalAddPost = true;
  }

  getAttachments2Edit(objectId: string) {
    this.documentService.getAttachmentsByObjectId(objectId, 4).subscribe({
      next: (res: any) => {
        this.fileList = res.data;
      }, error: (err: any) => {
        this.createErrorToast('Lỗi', err.message);
      }
    })
  }



  // onEditPost() {
  //   const data = {
  //     title: this.form.value.title,
  //     content: this.form.value.content,
  //     projectId: this.form.value.projectId?.key,
  //     id: this.data2edit.id
  //   }
  //   this.postService.editPost(data).subscribe({
  //     next: res => {
  //       this.buildForm();
  //       this.visibleModalAddPost = false;
  //       this.getPosts();
  //     }
  //   })
  // }

  onAddPost() {
    console.log(this.form)
    const data = {
      title: this.form.value.title,
      content: this.form.value.content,
      projectId: this.form.value.projectId?.key,
    }
    const formData = new FormData();
    this.fileList.forEach((file: any) => {
      formData.append('files', file);
    })
    formData.append('dto', new Blob([JSON.stringify(data)], {type: 'application/json'}))
    this.postService.createPost(formData).subscribe({
      next: res => {
        this.buildForm();
        this.fileList = [];
        this.visibleModalAddPost = false;
        this.getPosts();
      }
    })
  }

  onSelectFile($event: FileSelectEvent) {
    if (this.data2edit) {
      const formData = new FormData();
      formData.append('file', $event.currentFiles[$event.currentFiles.length - 1]);
      const dto = {
        objectId: this.data2edit.id,
        type: 1
      }
      formData.append('dto', new Blob([JSON.stringify(dto)], {type: 'application/json'}))
      this.documentService.addAttachment(formData).subscribe({
        next: (res: any) => {
          this.createSuccessToast('Thành công', 'Thêm tài liệu thành công');
          // this.getAttachments2Edit(this.data2edit.id);
        }, error: (err: any) => {
          this.createErrorToast('Lỗi', err.message);
        }
      })
    } else {
      this.fileList = $event.currentFiles;
    }
  }

  getFileTypeByName(name: string): any {
    return name.split('.').pop();
  }

  confirmDeleteFile(file: any, $event: MouseEvent) {

  }

  downloadAttachment(attachment: Attachment) {
    this.fileService.downloadFile(attachment.fileName, attachment.filePath)
  }

  openNewTab(item: any) {
    window.open(`/tasks/${item.id}/attachments/${item?.attachments[0].id}?type=4`, '_blank');
  }
}
