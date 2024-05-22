export class Filter {
  name!: string;
  subject!: string;
  projectId!: string;
  priorityIsEqual!: boolean;
  priority!: number;
  severityIsEqual!: boolean;
  severity!: number;
  typeIdIsEqual!: boolean;
  typeId!: string;
  statusIssueIdIsEqual!: boolean;
  statusIssueId!: string;
  categoryIdIsEqual!: boolean;
  categoryId!: string;
  assignUserIdIsEqual!: boolean;
  assignUserId!: string;
  reviewUserIdIsEqual!: boolean;
  reviewUserId!: string;
  startDateOperator!: string;
  startDate!: Date;
  endDateOperator!: string;
  endDate!: Date;
}
