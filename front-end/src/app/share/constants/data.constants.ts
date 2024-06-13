export const SEVERITIES: KeyValueModel[] = [
  {id: 1, name : 'Rất thấp'},
  {id: 2, name : 'Thấp'},
  {id: 3, name : 'Trung bình'},
  {id: 4, name : 'Cao'},
  {id: 5, name : 'Rất cao'},
]

export const PRIORIES: KeyValueModel[] = [
  {id: 1, name : 'Rất thấp'},
  {id: 2, name : 'Thấp'},
  {id: 3, name : 'Trung bình'},
  {id: 4, name : 'Cao'},
  {id: 5, name : 'Rất cao'},
]

export const PROFESSIONAL_LEVELS: KeyValueModel[] = [
  {id: 1, name : 'Intern'},
  {id: 2, name : 'Fresher'},
  {id: 3, name : 'Junior'},
  {id: 4, name : 'Middle'},
  {id: 5, name : 'Senior'},
  {id: 6, name : 'Leader'},
  {id: 7, name : 'Manager'},
]

export const PERMISSION: KeyValueModel[] = [
  {id: 1, name: 'Thành viên'},
  {id: 2, name: 'Leader'},
  {id: 3, name: 'Quản trị dự án'},
  {id: 4, name: 'Admin'}
]

export const GENDER: KeyValueModel[] = [
  {id: 1, name: 'Nam'},
  {id: 2, name: 'Nữ'},
  {id: 3, name: 'Khác'},
]

export const QUERY_OPERATOR: KeyValueModel[] = [
  {id: true, name: '='},
  {id: false, name: '!='},
]

export const QUERY_OPERATOR_DATE: KeyValueModel[] = [
  {id: 'bang', name: '='},
  {id: 'khac', name: '!='},
  {id: 'lon', name: '>'},
  {id: 'lonBang', name: '>='},
  {id: 'nho', name: '<'},
  {id: 'nhoBang', name: '<='},
]

export const NOTIFICATION_VALUE: KeyValueModel[] = [
  {id: 1, name: 'Bạn được thêm một công việc mới'},
  {id: 2, name: 'Công việc bạn được giao thực hiện đã được chỉnh sửa'}
]

export class KeyValueModel {
  id!: any
  name!: any
}
