import {Component, EventEmitter, Input, OnChanges, Output, SimpleChanges} from '@angular/core';

@Component({
  selector: 'app-sidebar-secondary',
  templateUrl: './sidebar-secondary.component.html',
  styleUrl: './sidebar-secondary.component.scss'
})
export class SidebarSecondaryComponent implements OnChanges{
  @Input() menuItems: MenuFileItem[] = [];
  @Input() showToolTips = true;
  @Input() params: any;
  @Output() handleSelect: EventEmitter<MenuFileItem> = new EventEmitter();
  selectedMenuItem: MenuFileItem | null = null;

  handleMenuItemClick(menuItem: MenuFileItem): void {
    // Xử lý khi một mục menu được chọn
    this.selectedMenuItem = menuItem;
    this.handleSelect.emit(menuItem);
    console.log(this.selectedMenuItem);
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['menuItems']) {
      console.log('menuItems:', this.menuItems);
      this.menuItems = changes['menuItems'].currentValue;
    }
  }
}

export interface MenuFileItem {
  name: any;
  urlPath: string;
  filePath: any;
  id: string;
  type: string;
  size: number;
}
