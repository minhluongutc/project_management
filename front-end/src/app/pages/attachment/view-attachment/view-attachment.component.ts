import {Component, Injector, OnInit} from '@angular/core';
import {UiModule} from "../../../share/ui/ui.module";
import {MenuFileItem} from "../../../share/ui/sidebar-secondary/sidebar-secondary.component";
import {BaseComponent} from "../../../share/ui/base-component/base.component";
import {Attachment} from "../../../models/attachment.model";
import {FileService} from "../../../share/services/file.service";
import {TaskService} from "../../../service/task.service";
import {DocumentService} from "../../../service/document.service";
import {NgxDocViewerModule} from "ngx-doc-viewer";

@Component({
  selector: 'app-view-attachment',
  templateUrl: './view-attachment.component.html',
  styleUrl: './view-attachment.component.scss',
  standalone: true,
  imports: [
    UiModule,
    NgxDocViewerModule
  ]
})
export class ViewAttachmentComponent extends BaseComponent implements OnInit {
  menuItems: MenuFileItem[] = []
  itemSelected: MenuFileItem = {name: '', urlPath: '', filePath: '', id: '', type: ''}
  taskId: any;
  fileList: any[] = [];

  constructor(injector: Injector,
              private taskService: TaskService,
              private fileService: FileService,
              private documentService: DocumentService) {
    super(injector);
    this.taskId = this.route.snapshot.params['id'];
  }

  async ngOnInit() {
    await this.getAttachments2Edit(this.taskId);
    this.getValueMenuItems();
  }

  async getAttachments2Edit(objectId: string) {
    try {
      const res: any = await this.documentService.getAttachmentsByObjectId(objectId).toPromise();
      this.fileList = res.data;
      console.log("fileList:", this.fileList);
    } catch (err: any) {
      this.createErrorToast('Lỗi', err.message);
    }
  }

  getValueMenuItems() {
    for (let file in this.fileList) {
      this.menuItems.push({
        name: this.fileList[file].name,
        urlPath: `/tasks/${this.taskId}/attachments/${this.fileList[file].id}`,
        filePath: this.fileList[file].filePath,
        id: this.fileList[file].id,
        type: this.fileList[file].type
      });
    }
    console.log(this.menuItems)
  }

  downloadAttachment(attachment: Attachment) {
    this.fileService.downloadFile(attachment.fileName, attachment.filePath)
  }

  getItemSelected(item: MenuFileItem) {
    this.itemSelected = item;
  }

  getFileUrl(id: string) {
    console.log(this.fileService.getFileUrl(id))
    return this.fileService.getFileUrl(id);
  }
}
