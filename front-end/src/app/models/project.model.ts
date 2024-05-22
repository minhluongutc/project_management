export class Project {
  id?: number;
  name!: string;
  description!: string;
  parentId?: number;
  warningTime?: number;
  dangerTime?: number;
}
