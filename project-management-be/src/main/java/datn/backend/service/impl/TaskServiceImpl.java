package datn.backend.service.impl;

import datn.backend.dto.*;
import datn.backend.entities.*;
import datn.backend.repositories.jpa.*;
import datn.backend.service.DocumentService;
import datn.backend.service.ProjectUserService;
import datn.backend.service.TaskService;
import datn.backend.utils.AuditUtils;
import datn.backend.utils.Constants;
import datn.backend.utils.excel.ExcelHelpers;
import datn.backend.utils.excel.TaskExcel;
import datn.backend.utils.exceptions.CustomException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static datn.backend.utils.excel.ExcelHelpers.cellValue2Date;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskServiceImpl implements TaskService {
    final TaskRepositoryJPA taskRepositoryJPA;
    final ProjectRepositoryJPA projectRepositoryJPA;
    final DocumentRepositoryJPA documentRepositoryJPA;
    final TypeRepositoryJPA typeRepositoryJPA;
    final StatusIssueRepositoryJPA statusIssueRepositoryJPA;
    final CategoryRepositoryJPA categoryRepositoryJPA;
    final UpdateHistoryTaskRepositoryJPA updateHistoryTaskRepositoryJPA;
    final UserCalendarRepositoryJPA userCalendarRepositoryJPA;

    final DocumentService documentService;
    final ProjectUserService projectUserService;

    final ModelMapper modelMapper;

    final ResourceLoader resourceLoader;

    @Override
    public Object getTasks(Authentication authentication, TaskDTO.TaskQueryDTO dto) {
        List<TaskDTO.TaskResponseDTO> taskListResponse = new ArrayList<>();
        List<TaskDTO.TaskResponseDTO> taskList = taskRepositoryJPA.getTasks(dto);
        for (TaskDTO.TaskResponseDTO task : taskList) {
            List<AttachmentDTO.AttachmentResponseDTO> attachments = documentRepositoryJPA.getAttachmentsByObjectIdAndType(task.getId(), Constants.DOCUMENT_TYPE.TASK.value);
            task.setAttachments(attachments);
            taskListResponse.add(task);
        }
        return taskListResponse;
    }

    @Override
    public Object getTasksAccordingLevel(Authentication authentication, TaskDTO.TaskQueryDTO dto) {
        List<TaskDTO.TaskResponseDTO> taskEntities = taskRepositoryJPA.getTasksLevel(dto, null, AuditUtils.getUserId(authentication));
        if (dto.getOtherTaskId() != null) {
            if (taskEntities.stream().anyMatch(task -> Objects.equals(task.getId(), dto.getOtherTaskId()))) {
                taskEntities.removeIf(task -> Objects.equals(task.getId(), dto.getOtherTaskId()));
            }
        }
        System.out.println(dto);
        List<TreeDTO> trees = new ArrayList<>();
        for (TaskDTO.TaskResponseDTO taskEntity : taskEntities) {
            TreeDTO treeDTO = new TreeDTO();
            treeDTO.setData(taskEntity);
            treeDTO.setChildren(new ArrayList<>());
            setTaskChildren(authentication, treeDTO, taskEntity, dto);
            trees.add(treeDTO);
        }
        return trees;
    }

    private void setTaskChildren(Authentication authentication, TreeDTO parentTree, TaskDTO.TaskResponseDTO taskDTO, TaskDTO.TaskQueryDTO dto) {
        List<TaskDTO.TaskResponseDTO> taskChildren = taskRepositoryJPA.getTasksLevel(dto, taskDTO.getId(), AuditUtils.getUserId(authentication));
        if (taskChildren.stream().anyMatch(task -> Objects.equals(task.getId(), dto.getOtherTaskId()))) {
            taskChildren.removeIf(task -> Objects.equals(task.getId(), dto.getOtherTaskId()));
        }
        parentTree.setChildren(new ArrayList<>());
        if (taskChildren.isEmpty()) return;
        for (TaskDTO.TaskResponseDTO taskChild : taskChildren) {
            TreeDTO treeDTO = new TreeDTO();
            treeDTO.setData(taskChild);
            setTaskChildren(authentication, treeDTO, taskChild, dto);
            parentTree.getChildren().add(treeDTO);
        }
    }

    @Override
    public Object getTasksChildrenByParentId(Authentication authentication, String parentId) {
        List<TaskDTO.TaskResponseGetChildren> taskEntities = taskRepositoryJPA.getTaskEntitiesByParentIdAndEnabled(parentId);
        List<TreeDTO> trees = new ArrayList<>();
        for (TaskDTO.TaskResponseGetChildren taskEntity : taskEntities) {
            TreeDTO treeDTO = new TreeDTO();
            treeDTO.setData(taskEntity);
            treeDTO.setChildren(new ArrayList<>());
            setTaskChildren(authentication, treeDTO, taskEntity);
            trees.add(treeDTO);
        }
        return trees;
    }

    private void setTaskChildren(Authentication authentication, TreeDTO parentTree, TaskDTO.TaskResponseGetChildren entity) {
        List<TaskDTO.TaskResponseGetChildren> taskChildren = taskRepositoryJPA.getTaskEntitiesByParentIdAndEnabled(entity.getId());
        parentTree.setChildren(new ArrayList<>());
        if (taskChildren.isEmpty()) return;
        for (TaskDTO.TaskResponseGetChildren taskChild : taskChildren) {
            TreeDTO treeDTO = new TreeDTO();
            treeDTO.setData(taskChild);
            setTaskChildren(authentication, treeDTO, taskChild);
            parentTree.getChildren().add(treeDTO);
        }
    }

    @Override
    public Object insertTask(Authentication authentication, TaskDTO.TaskInsertDTO dto, MultipartFile[] files) {
        // save entity
        TaskEntity taskEntity = new TaskEntity();
        taskEntity = modelMapper.map(dto, TaskEntity.class);
        taskEntity.setTaskCode(generateTaskCodeUniqueEachProject(dto.getProjectId()));
//        taskEntity.setId(AuditUtils.generateUUID());
        taskEntity.setCreateUserId(AuditUtils.createUserId(authentication));
        taskEntity.setCreateTime(AuditUtils.createTime());
        taskEntity.setEnabled(AuditUtils.enable());
        taskRepositoryJPA.save(taskEntity);

        // save file
        if (files != null) {
            documentService.addAttachments(authentication, taskEntity.getId(), files, Constants.DOCUMENT_TYPE.TASK.value);
        }
        return "Thành công";
    }

    @Override
    @Transactional
    public Object updateTask(Authentication authentication, TaskDTO.TaskUpdateDTO dto, String id) {
        TaskEntity taskEntity = taskRepositoryJPA.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
        addUpdateHistoryTask(authentication, taskEntity, dto);
        taskEntity.setSubject(dto.getSubject());
        taskEntity.setDescription(dto.getDescription());
        taskEntity.setIsPublic(dto.getIsPublic());
        taskEntity.setTypeId(dto.getTypeId());
        taskEntity.setStatusIssueId(dto.getStatusIssueId());
        taskEntity.setPriority(dto.getPriority());
        taskEntity.setSeverity(dto.getSeverity());
        taskEntity.setParentId(dto.getParentId());
        taskEntity.setAssignUserId(dto.getAssignUserId());
        taskEntity.setReviewUserId(dto.getReviewUserId());
        taskEntity.setStartDate(dto.getStartDate());
        taskEntity.setDueDate(dto.getDueDate());
        taskEntity.setEstimateTime(dto.getEstimateTime());
        taskEntity.setCategoryId(dto.getCategoryId());
        taskEntity.setUpdateUserId(AuditUtils.updateUserId(authentication));
        taskEntity.setUpdateTime(AuditUtils.updateTime());
        taskRepositoryJPA.save(taskEntity);
        return "successfully updated";
    }

    private void addUpdateHistoryTask(Authentication authentication, TaskEntity oldValue, TaskDTO.TaskUpdateDTO newValue) {
        UpdateHistoryTaskEntity updateHistoryTaskEntity = new UpdateHistoryTaskEntity();
        updateHistoryTaskEntity.setId(AuditUtils.generateUUID());
        updateHistoryTaskEntity.setTaskId(oldValue.getId());
        if (!Objects.equals(newValue.getSubject(), oldValue.getSubject())) {
            updateHistoryTaskEntity.setOldSubject(oldValue.getSubject());
            updateHistoryTaskEntity.setNewSubject(newValue.getSubject());
        }
        if (!Objects.equals(newValue.getDescription(), oldValue.getDescription())) {
            updateHistoryTaskEntity.setOldDescription(oldValue.getDescription());
            updateHistoryTaskEntity.setNewDescription(newValue.getDescription());
        }
        if (!Objects.equals(newValue.getTypeId(), oldValue.getTypeId())) {
            updateHistoryTaskEntity.setOldTypeId(oldValue.getTypeId());
            updateHistoryTaskEntity.setNewTypeId(newValue.getTypeId());
        }
        if (!Objects.equals(newValue.getStatusIssueId(), oldValue.getStatusIssueId())) {
            updateHistoryTaskEntity.setOldStatusIssueId(oldValue.getStatusIssueId());
            updateHistoryTaskEntity.setNewStatusIssueId(newValue.getStatusIssueId());
        }
        if (!Objects.equals(newValue.getPriority(), oldValue.getPriority())) {
            updateHistoryTaskEntity.setOldPriority(oldValue.getPriority());
            updateHistoryTaskEntity.setNewPriority(newValue.getPriority());
        }
        if (!Objects.equals(newValue.getSeverity(), oldValue.getSeverity())) {
            updateHistoryTaskEntity.setOldSeverity(oldValue.getSeverity());
            updateHistoryTaskEntity.setNewSeverity(newValue.getSeverity());
        }
        if (!Objects.equals(newValue.getParentId(), oldValue.getParentId())) {
            updateHistoryTaskEntity.setOldParentId(oldValue.getParentId());
            updateHistoryTaskEntity.setNewParentId(newValue.getParentId());
        }
        // todo fix null pointer exception
        if (oldValue.getStartDate() != null && newValue.getStartDate() != null) {
            LocalDateTime oldStartDate = oldValue.getStartDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime()
                    .truncatedTo(ChronoUnit.SECONDS);

            LocalDateTime newStartDate = newValue.getStartDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime()
                    .truncatedTo(ChronoUnit.SECONDS);

            if (!oldStartDate.equals(newStartDate)) {
                updateHistoryTaskEntity.setOldStartDate(oldValue.getStartDate());
                updateHistoryTaskEntity.setNewStartDate(newValue.getStartDate());
            }
        }
        // todo fix null pointer exception
        if (oldValue.getDueDate() != null && newValue.getDueDate() != null) {
            LocalDateTime oldDueDate = oldValue.getDueDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime()
                    .truncatedTo(ChronoUnit.SECONDS);

            LocalDateTime newDueDate = newValue.getDueDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime()
                    .truncatedTo(ChronoUnit.SECONDS);

            if (!oldDueDate.equals(newDueDate)) {
                updateHistoryTaskEntity.setOldDueDate(oldValue.getDueDate());
                updateHistoryTaskEntity.setNewDueDate(newValue.getDueDate());
            }
        }
        if (!Objects.equals(newValue.getAssignUserId(), oldValue.getAssignUserId())) {
            updateHistoryTaskEntity.setOldAssignUserId(oldValue.getAssignUserId());
            updateHistoryTaskEntity.setNewAssignUserId(newValue.getAssignUserId());
        }
        if (!Objects.equals(newValue.getReviewUserId(), oldValue.getReviewUserId())) {
            updateHistoryTaskEntity.setOldReviewUserId(oldValue.getReviewUserId());
            updateHistoryTaskEntity.setNewReviewUserId(newValue.getReviewUserId());
        }
        if (!Objects.equals(newValue.getCategoryId(), oldValue.getCategoryId())) {
            updateHistoryTaskEntity.setOldCategoryId(oldValue.getCategoryId());
            updateHistoryTaskEntity.setNewCategoryId(newValue.getCategoryId());
        }
        if (!Objects.equals(newValue.getEstimateTime(), oldValue.getEstimateTime())) {
            updateHistoryTaskEntity.setOldEstimateTime(oldValue.getEstimateTime());
            updateHistoryTaskEntity.setNewEstimateTime(newValue.getEstimateTime());
        }
        updateHistoryTaskEntity.setModifyUserId(AuditUtils.updateUserId(authentication));
        updateHistoryTaskEntity.setModifyTime(AuditUtils.updateTime());
        updateHistoryTaskRepositoryJPA.save(updateHistoryTaskEntity);
    }

    @Override
    public Object getTask(Authentication authentication, String id) {
        return taskRepositoryJPA.getTaskDetail(id).orElseThrow(() -> new RuntimeException("Task not found"));
    }

    @Override
    public Object getTasksChildren(Authentication authentication, String parentId) {
        return taskRepositoryJPA.getChildrenTaskByParentId(parentId);
    }

    @Override
    public void downloadTemplate(Authentication authentication, String projectId, HttpServletResponse response) {
        if (!StringUtils.isEmpty(projectId)) {
            List<TypeEntity> listType = typeRepositoryJPA.getTypes(projectId, null);
            List<UserDTO.UserResponseDTO> ListUser = projectUserService.getUsers(projectId);
            List<StatusIssueEntity> statusIssueEntities = statusIssueRepositoryJPA.getStatusIssue(projectId, null);
            List<CategoryEntity> categoryEntities = categoryRepositoryJPA.getCategories(projectId, null);
            List<TaskEntity> taskEntities = taskRepositoryJPA.getTaskEntitiesByProjectIdAndEnabled(projectId, Constants.STATUS.ACTIVE.value);
            TaskExcel.templateImportTask(listType, ListUser, statusIssueEntities, categoryEntities, taskEntities, response);
        }
    }

    @Override
    public UploadMessageDTO importTasks(Authentication authentication, String projectId, MultipartFile file) {
        byte[] byteArr;

        try {
            byteArr = file.getBytes();
        } catch (IOException e) {
            throw new CustomException("Lỗi khi đọc file");
        }

        String fileName = file.getOriginalFilename();
        assert fileName != null;
        byte[] bytes;
        UploadMessageDTO message = new UploadMessageDTO();
        try (Workbook workbook = WorkbookFactory.create(new ByteArrayInputStream(byteArr))) {
            Sheet sheet = workbook.getSheetAt(0);
            // Validate match template
            boolean isMatchTemplate = true;
            Resource resourceTemplateFile = resourceLoader.getResource("classpath:templates/template_ImportTasks.xlsx");

            try (Workbook workbookTemplate = new XSSFWorkbook(new FileInputStream(resourceTemplateFile.getFile()))) {
                Sheet sheetTemplate = workbookTemplate.getSheetAt(0);
                if (!ExcelHelpers.rowsAreEqual(sheet.getRow(0), sheetTemplate.getRow(0))) {
                    isMatchTemplate = false;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (!isMatchTemplate) throw new CustomException("Sai định dạng với template");
            Sheet loaiCongViec = workbook.getSheetAt(2);
            int lastRowType = loaiCongViec.getLastRowNum();
            int lastRowUser = workbook.getSheetAt(3).getLastRowNum();
            int lastRowStatus = workbook.getSheetAt(4).getLastRowNum();
            int lastRowCategory = workbook.getSheetAt(5).getLastRowNum();
            int lastRowTask = workbook.getSheetAt(6).getLastRowNum();
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row formulaRow = sheet.getRow(i);
                if (formulaRow == null) formulaRow = sheet.createRow(i);

                // Set the VLOOKUP formula in the formula cell
                // Cell P-15
                Cell formulaCellP = formulaRow.createCell(15);
                if (formulaCellP == null) formulaCellP = formulaRow.createCell(15);
                formulaCellP.setCellFormula("IFERROR(VLOOKUP(A" + (i + 1) + ",'LoaiCongViec'!A2:B" + (lastRowType + 1) + ",2,FALSE),\"\")");
                // Cell Q-16
                Cell formulaCellQ = formulaRow.createCell(16);
                if (formulaCellQ == null) formulaCellQ = formulaRow.createCell(16);
                formulaCellQ.setCellFormula("IFERROR(VLOOKUP(I" + (i + 1) + ",'ThanhVien'!A2:B" + (lastRowUser + 1) + ",2,FALSE),\"\")");

                Cell formulaCellQ2 = formulaRow.createCell(20);
                if (formulaCellQ2 == null) formulaCellQ2 = formulaRow.createCell(20);
                formulaCellQ2.setCellFormula("IFERROR(VLOOKUP(J" + (i + 1) + ",'ThanhVien'!A2:B" + (lastRowUser + 1) + ",2,FALSE),\"\")");
                // Cell R-17
                Cell formulaCellR = formulaRow.createCell(17);
                if (formulaCellR == null) formulaCellR = formulaRow.createCell(17);
                formulaCellR.setCellFormula("IFERROR(VLOOKUP(K" + (i + 1) + ",'TrangThai'!A2:B" + (lastRowStatus + 1) + ",2,FALSE),\"\")");
                // Cell S-18
                Cell formulaCellS = formulaRow.createCell(18);
                if (formulaCellS == null) formulaCellS = formulaRow.createCell(18);
                formulaCellS.setCellFormula("IFERROR(VLOOKUP(L" + (i + 1) + ",'DanhMuc'!A2:B" + (lastRowCategory + 1) + ",2,FALSE),\"\")");
                // Cell T-19
                Cell formulaCellT = formulaRow.createCell(19);
                if (formulaCellT == null) formulaCellT = formulaRow.createCell(19);
                formulaCellT.setCellFormula("IFERROR(VLOOKUP(M" + (i + 1) + ",'CongViec'!A2:B" + (lastRowTask + 1) + ",2,FALSE),\"\")");

                evaluator.evaluateFormulaCell(formulaCellP);
                evaluator.evaluateFormulaCell(formulaCellQ);
                evaluator.evaluateFormulaCell(formulaCellR);
                evaluator.evaluateFormulaCell(formulaCellS);
                evaluator.evaluateFormulaCell(formulaCellT);
            }

            int totalRows = sheet.getPhysicalNumberOfRows();
            System.out.println("importTasks sheet row: " + totalRows);

            if (totalRows > 1003) throw new CustomException("Tối đa tổng số bản ghi import là 1000");
            int totalInsert = 0;
            int totalError = 0;

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);

                if (validateFileImport(row, projectId)) {
                    TaskEntity taskEntity = new TaskEntity();
                    taskEntity.setId(AuditUtils.generateUUID());
                    taskEntity.setProjectId(projectId);
                    taskEntity.setTaskCode(generateTaskCodeUniqueEachProject(projectId));

                    taskEntity.setTypeId(ExcelHelpers.getStringCellValue(row.getCell(15)));
                    taskEntity.setSubject(ExcelHelpers.getStringCellValue(row.getCell(1)));
                    taskEntity.setDescription(ExcelHelpers.getStringCellValue(row.getCell(2)));
                    taskEntity.setStartDate(cellValue2Date(row.getCell(3))); //todo...
                    taskEntity.setDueDate(cellValue2Date(row.getCell(4))); //todo...
                    taskEntity.setEstimateTime(Integer.parseInt(Objects.requireNonNull(ExcelHelpers.getStringCellValue(row.getCell(5)))));
                    taskEntity.setPriority(getValuePriorityAndSeverity(Objects.requireNonNull(ExcelHelpers.getStringCellValue(row.getCell(6)))));
                    taskEntity.setSeverity(getValuePriorityAndSeverity(Objects.requireNonNull(ExcelHelpers.getStringCellValue(row.getCell(7)))));
                    taskEntity.setAssignUserId(ExcelHelpers.getStringCellValue(row.getCell(16)));
                    taskEntity.setReviewUserId(ExcelHelpers.getStringCellValue(row.getCell(16)));
                    taskEntity.setStatusIssueId(ExcelHelpers.getStringCellValue(row.getCell(17)));
                    taskEntity.setCategoryId(ExcelHelpers.getStringCellValue(row.getCell(18)));
                    taskEntity.setParentId(ExcelHelpers.getStringCellValue(row.getCell(19)));
                    taskEntity.setCreateTime(AuditUtils.createTime());
                    taskEntity.setCreateUserId(AuditUtils.createUserId(authentication));
                    taskEntity.setEnabled(Constants.STATUS.ACTIVE.value);
                    taskRepositoryJPA.save(taskEntity);
                    totalInsert++;
                } else {
                    totalError++;
                }
            }
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            workbook.write(os);
            bytes = os.toByteArray();
            message.setTotalError(totalError);
            message.setTotalSuccess(totalInsert);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                for (int j = 15; j <= 20; j++) {
                    if (row.getCell(j) != null)
                        row.getCell(j).setCellValue("");
                }
            }

            if (totalError > 0) {
                String dataExport = Base64.getEncoder().encodeToString(bytes);
                message.setMessageCode("001");
                message.setMessageDesc("Thêm mới: " + totalInsert + " | Lỗi: " + totalError);
                message.setFileData(dataExport);
            } else {
                message.setMessageCode("000");
                message.setMessageDesc("Thêm mới: " + totalInsert);
            }

        } catch (Exception e) {
//            logger.error("Import noi dung khong thanh cong ", e);
            if (e instanceof CustomException) throw new CustomException(e.getMessage());
        }

        return message;
    }

    @Override
    public Object getUserCalendarTask(Authentication authentication, String projectId) {
        List<TaskEntity> tasks = taskRepositoryJPA.getTaskEntitiesByProjectIdAndAssignUserId(projectId, AuditUtils.getUserId(authentication));
        String[] TaskInCalendar = userCalendarRepositoryJPA.getUserCalendars(AuditUtils.getUserId(authentication), projectId).stream().map(UserCalendarResponseDTO::getTaskId).toArray(String[]::new);
        // get tasks in tasks but not in TaskInCalendar
        return tasks.stream().filter(task -> !Arrays.asList(TaskInCalendar).contains(task.getId())).toList();
    }

    @Override
    public Object getTaskStatistics(Authentication authentication, String projectId, String userId) {

        List<TaskEntity> allTask = taskRepositoryJPA.getTaskEntitiesByProjectId(projectId);
        if (userId != null && !userId.isEmpty()) {
            allTask = allTask.stream().filter(task -> Objects.equals(task.getAssignUserId(), userId)).toList();
        }
        List<TaskEntity> taskLateNotDone = new ArrayList<>();
        List<TaskEntity> taskDoneInTime = new ArrayList<>();
        List<TaskEntity> taskLateDone = new ArrayList<>();
        List<TaskEntity> taskToDo = new ArrayList<>();
        for (TaskEntity task : allTask) {
            if (task.getStatusIssueId() != null) {
                StatusIssueEntity statusDone = statusIssueRepositoryJPA.getStatusIssueEntitiesByIdAndCodeAndEnabled(task.getStatusIssueId(), Constants.STATUS_ISSUE.DONE.value, Constants.STATUS.ACTIVE.value);

                if (statusDone != null) { // nếu công việc đang ở trạng thái Done
                    if (task.getDueDate() != null) {
                        if (task.getDueDate().before(statusDone.getCreateTime())) { // nếu công việc hoàn thành trễ
                            taskLateDone.add(task);
                        } else { // nếu công việc hoàn thành đúng hạn
                            taskDoneInTime.add(task);
                        }
                    } else {
                        taskDoneInTime.add(task);
                    }
                } else { // nếu công việc chưa hoàn thành
                    if (task.getDueDate() != null) {
                        if (task.getDueDate().before(new Date())) {
                            taskLateNotDone.add(task);
                        } else {
                            taskToDo.add(task);
                        }
                    } else {
                        taskToDo.add(task);
                    }
                }
            }
        }
        return new StatisticsDTO(
                taskDoneInTime.size(),
                taskLateNotDone.size(),
                taskLateDone.size(),
                taskToDo.size()
        );

//        return new StatisticsDTO(
//                calculatePercent(taskDoneInTime.size(), allTask.size()),
//                calculatePercent(taskLateNotDone.size(), allTask.size()),
//                calculatePercent(taskLateDone.size(), allTask.size()),
//                calculatePercent(taskToDo.size(), allTask.size())
//        );
    }

    @Override
    public Object deleteTask(Authentication authentication, String id) {
        TaskEntity taskEntity = taskRepositoryJPA.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
        taskEntity.setEnabled(Constants.STATUS.IN_ACTIVE.value);
        taskEntity.setUpdateUserId(AuditUtils.updateUserId(authentication));
        taskEntity.setUpdateTime(AuditUtils.updateTime());
        taskRepositoryJPA.save(taskEntity);
        return "successfully deleted";
    }

    private float calculatePercent(int value, int total) {
        if (total == 0) {
            throw new IllegalArgumentException("Tổng không thể bằng 0");
        }
        return ((float) value / total) * 100;
    }

    private boolean validateFileImport(Row row, String projectId) throws ParseException {
        List<TypeEntity> listType = typeRepositoryJPA.getTypes(projectId, null);
        List<UserDTO.UserResponseDTO> ListUser = projectUserService.getUsers(projectId);
        List<StatusIssueEntity> statusIssueEntities = statusIssueRepositoryJPA.getStatusIssue(projectId, null);
        List<CategoryEntity> categoryEntities = categoryRepositoryJPA.getCategories(projectId, null);
        List<TaskEntity> taskEntities = taskRepositoryJPA.getTaskEntitiesByProjectIdAndEnabled(projectId, Constants.STATUS.ACTIVE.value);

        String msg = "";

        if (Objects.equals(ExcelHelpers.getStringCellValue(row.getCell(0)), "")) {
            msg += " | Loại công việc không được để trống";
        } else {
            if (listType.stream().noneMatch(typeEntity -> Objects.equals(typeEntity.getName(), ExcelHelpers.getStringCellValue(row.getCell(0))))) {
                msg += " | Loại công việc không tồn tại";
            }
        }

        if (Objects.equals(ExcelHelpers.getStringCellValue(row.getCell(1)), ""))
            msg += " | Tiêu đề không được để trống";

        try {
            cellValue2Date(row.getCell(3));
        } catch (ParseException e) {
            msg += " | Ngày bắt đầu không đúng định dạng";
        }

        try {
            cellValue2Date(row.getCell(4));
        } catch (ParseException e) {
            msg += " | Ngày kết thúc không đúng định dạng";
        }

        try {
            if (!Objects.equals(ExcelHelpers.getStringCellValue(row.getCell(5)), "")) {
                if (Integer.parseInt(Objects.requireNonNull(ExcelHelpers.getStringCellValue(row.getCell(5)))) < 0) {
                    msg += " | Thời gian ước lượng không được nhỏ hơn 0";
                }
            }
        } catch (NumberFormatException e) {
            msg += " | Thời gian ước lượng không đúng định dạng số nguyên";
        }

//        if (Objects.equals(ExcelHelpers.getStringCellValue(row.getCell(6)), ""))
//            msg += "Mức độ ưu tiên không được để trống";
//
//        if (Objects.equals(ExcelHelpers.getStringCellValue(row.getCell(7)), ""))
//            msg += "Mức độ nghiêm trọng không được để trống";

        if (Objects.equals(ExcelHelpers.getStringCellValue(row.getCell(8)), "")) {
            msg += " | Người được giao không được để trông";
        } else {
            if (ListUser.stream().noneMatch(user -> Objects.equals(user.getId(), ExcelHelpers.getStringCellValue(row.getCell(16))))) {
                msg += " | Người được giao không tồn tại";
            }
        }

        if (Objects.equals(ExcelHelpers.getStringCellValue(row.getCell(9)), "")) {
            msg += " | Người nghiệm thu không được để trống";
        } else {
            if (ListUser.stream().noneMatch(user -> Objects.equals(user.getId(), ExcelHelpers.getStringCellValue(row.getCell(16))))) {
                msg += " | Người nghiệm thu không tồn tại";
            }
        }

        if (Objects.equals(ExcelHelpers.getStringCellValue(row.getCell(10)), "")) {
            msg += " | Trạng thái công việc không được để trống";
        } else {
            if (statusIssueEntities.stream().noneMatch(statusIssueEntity -> Objects.equals(statusIssueEntity.getId(), ExcelHelpers.getStringCellValue(row.getCell(17))))) {
                msg += " | Trạng thái công việc không tồn tại";
            }
        }

        if (Objects.equals(ExcelHelpers.getStringCellValue(row.getCell(11)), "")) {
            msg += " | Danh mục không được để trống";
        } else {
            if (categoryEntities.stream().noneMatch(categoryEntity -> Objects.equals(categoryEntity.getId(), ExcelHelpers.getStringCellValue(row.getCell(18))))) {
                msg += " | Danh mục không tồn tại";
            }
        }

        if (!msg.isEmpty()) {
            writeResult(row, "Failed", msg);
            return false;
        }
        return true;
    }

    private void writeResult(Row row, String resultCode, String resultMgs) {
        Cell result = row.createCell(13);
        result.setCellType(CellType.STRING);
        result.setCellValue(resultCode);
        Cell description = row.createCell(14);
        description.setCellType(CellType.STRING);
        description.setCellValue(resultMgs);
    }

    private String generateTaskCodeUniqueEachProject(String projectId) {
        Random random = new Random();
        List<String> taskCodes = taskRepositoryJPA.getTaskEntitiesByProjectId(projectId).stream().map(TaskEntity::getTaskCode).toList();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(projectRepositoryJPA.getProjectCodeById(projectId)).append("-");
        for (int i = 0; i < 6; i++) {
            stringBuilder.append(random.nextInt(10)); // Random số từ 0 đến 9
        }
        if (taskCodes.contains(stringBuilder.toString())) {
            generateTaskCodeUniqueEachProject(projectId);
        }
        return stringBuilder.toString();
    }

    public Integer getValuePriorityAndSeverity(String name) {
        return switch (name) {
            case "Rất thấp" -> 1;
            case "Thấp" -> 2;
            case "Trung bình" -> 3;
            case "Cao" -> 4;
            case "Rất cao" -> 5;
            default -> 0;
        };
    }

    public Object getTaskCompletionRate(Authentication authentication, String userId, String projectId) {
        List<TaskEntity> taskEntities = taskRepositoryJPA.getTaskEntitiesByProjectIdAndAssignUserIdAndEnabled(projectId, userId, Constants.STATUS.ACTIVE.value);

        int expiredTask = 0;
        int lateCompleteTask = 0;

        int onTimeCompleteTask = 0;
        for (TaskEntity taskEntity : taskEntities) {
            if (taskEntity.getStatusIssueId() != null) {
                StatusIssueEntity statusDone = statusIssueRepositoryJPA.getStatusIssueEntitiesByIdAndCodeAndEnabled(taskEntity.getStatusIssueId(), Constants.STATUS_ISSUE.DONE.value, Constants.STATUS.ACTIVE.value);
                if (statusDone != null) { // nếu công việc đang ở trạng thái Done

                } else {

                }
            }
        }
        return null;
    }

    private Boolean isExpiredTaskDone(Date dueDate, Date doneTime) {
        // compare doneTime with dueDate
        if (doneTime == null) return false;
        return doneTime.after(dueDate);
    }
}
