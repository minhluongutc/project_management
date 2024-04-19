import {Validators} from "@angular/forms";

export class Task {
  id?: string;
  projectId!: string;
  subject!: string;
  description!: string;
  startDate!: Date;
  dueDate!: Date;
  parentId!: string;
  typeId!: string;
  estimateTime!: string;
  priority!: string;
  severity!: string;
  assignUserId!: string;
  reviewUserId!: string;
  statusIssueId!: string;
  categoryId!: string;
  reporter!: string;
  isPublic!: string;
}
