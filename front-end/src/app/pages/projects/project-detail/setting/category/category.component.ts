import {Component, ElementRef, Injector, OnInit, ViewChild} from '@angular/core';
import {BaseComponent} from "../../../../../share/ui/base-component/base.component";
import {CategoryService} from "../../../../../service/category.service";
import {EditableRow} from "primeng/table";
import {Category} from "../../../../../models/category.model";
import {ConfirmationService} from "primeng/api";

@Component({
  selector: 'app-category',
  templateUrl: './category.component.html',
  styleUrl: './category.component.scss',
  providers: [EditableRow, ConfirmationService]
})
export class CategoryComponent extends BaseComponent implements OnInit {
  projectId: any;
  keySearch: string = '';

  clonedValue!: Category

  @ViewChild('addButton') addButton: any;
  @ViewChild('focusButton') focusButton: any;

  constructor(injector: Injector,
              private categoryService: CategoryService,
              private confirmationService: ConfirmationService,
  ) {
    super(injector);
    console.log(this.route.snapshot?.parent?.paramMap.get('id'))
    this.projectId = this.route.snapshot?.parent?.paramMap.get('id') || null;
  }

  ngOnInit(): void {
    this.getCategories();
  }

  getCategories() {
    let data: any = {
      projectId: this.projectId
    }
    if (this.keySearch) {
      data = {...data, keySearch: this.keySearch.trim()}
    }
    this.categoryService.getCategories(data).subscribe(res => {
      this.listData = res.data;
    });
  }

  onRowEditInit(item: any) {
    this.clonedValue = { ...item };
  }

  onRowEditSave(item: any) {
    console.log(this.clonedValue)
    const data: Category = {
      id: item.id,
      name: item.name.trim(),
      description: item.description.trim(),
      projectId: item.projectId
    };
    if (this.clonedValue.name !== item.name || this.clonedValue.description !== item.description) {
      item.id ? this.updateCategory(data) : this.createCategory(data);
    }
  }

  confirmDelete(item: any, event: Event) {
    console.log(event)
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: 'bạn có muốn xóa danh mục này?',
      header: 'Xác nhận xóa',
      icon: 'pi pi-info-circle',
      acceptButtonStyleClass: "p-button-danger p-button-text",
      rejectButtonStyleClass: "p-button-text p-button-text",
      acceptIcon: "none",
      rejectIcon: "none",

      accept: () => {
        this.deleteCategory(item.id)
      },
      reject: () => {
        console.log("is reject")
      }
    });
  }

  createCategory(item: Category) {
    this.categoryService.createCategory(item).subscribe({
      next: (res) => {
        this.getCategories();
        this.createSuccessToast('Thành công','Thêm mới danh mục thành công');
      },
      error: (err) => {
        this.createErrorToast('Lỗi', 'Thêm mới danh mục thất bại');
      }
    });
  }

  updateCategory(item: Category) {
    this.categoryService.updateCategory(item.id, item).subscribe({
      next: (res) => {
        this.getCategories();
        this.createSuccessToast('Thành công','Chỉnh sửa danh mục thành công');
      },
      error: (err) => {
        this.createErrorToast('Lỗi', 'Chỉnh sửa danh mục thất bại');
      }
    });
  }

  deleteCategory(id: string) {
    this.categoryService.deleteCategory(id).subscribe({
      next: (res) => {
        this.getCategories();
        this.createSuccessToast('Thành công','Xóa danh mục thành công');
      },
      error: (err) => {
        this.createErrorToast('Lỗi', 'Xóa danh mục thất bại');
      }
    });
  }

  async addRow2Table() {
    const row: Category = {
      id: '',
      name: '',
      description: '',
      projectId: this.projectId
    }
    this.listData.unshift(row)
    setTimeout(() => {
      this.addButton.nativeElement.click();
      setTimeout(() => {
        this.focusButton.nativeElement.focus();
      }, 50);
    }, 50);
  }

  onRowEditCancel(item: any) {
    if (!item.id) {
      this.listData.shift();
    }
  }
}
