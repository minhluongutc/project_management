package datn.backend.utils.excel;

import datn.backend.dto.TaskDTO;
import datn.backend.dto.UserDTO;
import datn.backend.entities.*;
import datn.backend.utils.Constants;
import datn.backend.utils.exceptions.CustomException;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class TaskExcel extends ExcelHelpers {


    public static void templateImportTask(List<TypeEntity> types, List<UserDTO.UserResponseDTO> users, List<StatusIssueEntity> statuses, List<CategoryEntity> categories, List<TaskEntity> tasks, HttpServletResponse response) {
        Resource resource = new ClassPathResource("templates/template_ImportTasks.xlsx");
        File file = null;
        try {
            file = resource.getFile();
        } catch (IOException e) {
            throw new CustomException("File không tồn tại!");
        }
        try (Workbook workbook = new XSSFWorkbook(new FileInputStream(file))) {
            // types
            writeListTypeToTemplate(workbook, 2, types, 0);
            Name listType = workbook.createName();
            listType.setNameName("DS_LOAI_CONG_VIEC");
            String listTypeReference = "'LoaiCongViec'!$A$2:$A$" + (types.size() + 1);
            listType.setRefersToFormula(listTypeReference);
            // priority
            Name listPriority = workbook.createName();
            listPriority.setNameName("DS_DO_UU_TIEN");
            listPriority.setRefersToFormula("DoUuTienVaNghiemTrong!$A$2:$A$5");
            //severity
            Name listSeverity = workbook.createName();
            listSeverity.setNameName("DS_DO_NGHIEM_TRONG");
            listSeverity.setRefersToFormula("DoUuTienVaNghiemTrong!$B$2:$B$5");
            // user
            writeListUserToTemplate(workbook, 3, users, 0);
            Name listUser = workbook.createName();
            listUser.setNameName("DS_NGUOI_DUNG");
            String listUserReference = "'ThanhVien'!$A$2:$A$" + (users.size() + 1);
            listUser.setRefersToFormula(listUserReference);
            // status
            writeListStatusIssueToTemplate(workbook, 4, statuses, 0);
            Name listStatus = workbook.createName();
            listStatus.setNameName("DS_TRANG_THAI");
            String listStatusReference = "'TrangThai'!$A$2:$A$" + (statuses.size() + 1);
            listStatus.setRefersToFormula(listStatusReference);
            // category
            writeListCategoryToTemplate(workbook, 5, categories, 0);
            Name listCategory = workbook.createName();
            listCategory.setNameName("DS_DANH_MUC");
            String listCategoryReference = "'DanhMuc'!$A$2:$A$" + (categories.size() + 1);
            listCategory.setRefersToFormula(listCategoryReference);
            // task (to chose parent task)
            writeListTaskToTemplate(workbook, 6, tasks, 0);
            Name listTask = workbook.createName();
            listTask.setNameName("DS_CONG_VIEC");
            String listTaskReference = "'CongViec'!$A$2:$A$" + (tasks.size() + 1);
            listTask.setRefersToFormula(listTaskReference);


            Sheet sheet = workbook.getSheetAt(0);
            DataValidation typeValidation = getDataValidation(sheet, "DS_LOAI_CONG_VIEC", 0);
            DataValidation priorityValidation = getDataValidation(sheet, "DS_DO_UU_TIEN", 6);
            DataValidation severityValidation = getDataValidation(sheet, "DS_DO_NGHIEM_TRONG", 7);
            DataValidation userAssigneeValidation = getDataValidation(sheet, "DS_NGUOI_DUNG", 8);
            DataValidation userReportValidation = getDataValidation(sheet, "DS_NGUOI_DUNG", 9);
            DataValidation statusValidation = getDataValidation(sheet, "DS_TRANG_THAI", 10);
            DataValidation categoryValidation = getDataValidation(sheet, "DS_DANH_MUC", 11);
            DataValidation parentTaskValidation = getDataValidation(sheet, "DS_CONG_VIEC", 12);

            sheet.addValidationData(typeValidation);
            sheet.addValidationData(priorityValidation);
            sheet.addValidationData(severityValidation);
            sheet.addValidationData(userAssigneeValidation);
            sheet.addValidationData(userReportValidation);
            sheet.addValidationData(statusValidation);
            sheet.addValidationData(categoryValidation);
            sheet.addValidationData(parentTaskValidation);

            workbook.setForceFormulaRecalculation(true);
            CellStyle cellStyle = workbook.createCellStyle();
            CreationHelper createHelper = workbook.getCreationHelper();
            cellStyle.setDataFormat(createHelper.createDataFormat().getFormat(Constants.DATE_STRING_FORMAT));
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row formulaRow = sheet.getRow(i);
                Cell formulaCell3 = formulaRow.getCell(3); // Cell B1
                Cell formulaCell4 = formulaRow.getCell(4); // Cell B1
                if (formulaCell3 == null) formulaCell3 = formulaRow.createCell(1);
                formulaCell3.setCellValue(new Date());
                formulaCell3.setCellStyle(cellStyle);
                formulaCell4.setCellValue(new Date());
                formulaCell4.setCellStyle(cellStyle);
            }
            exportStream(workbook, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private static @NotNull DataValidation getDataValidation(Sheet sheet, String constantName, Integer column) {
        DataValidationHelper dvHelper = sheet.getDataValidationHelper();
        // type
        DataValidationConstraint typeConstraint = dvHelper.createFormulaListConstraint(constantName);
        CellRangeAddressList addressList = new CellRangeAddressList(1, 1001, column, column); // E3 - E1003
        DataValidation validation = dvHelper.createValidation(typeConstraint, addressList);
        validation.setShowErrorBox(true);
        validation.setErrorStyle(DataValidation.ErrorStyle.STOP);
        validation.createErrorBox("Invalid Option", "Please select a valid option from the list.");
        return validation;
    }

    public static void writeListTypeToTemplate(Workbook workbook, int index, List<TypeEntity> types, int columnCount) {
        Sheet sheet = workbook.getSheetAt(index);
        int rowIndex = 1;
        for (var item : types) {
            Row row = sheet.getRow(rowIndex);
            if (row == null) row = sheet.createRow(rowIndex);
            writeCell(sheet, row, columnCount, item.getName());
            writeCell(sheet, row, columnCount + 1, item.getId());
            rowIndex++;
        }
        autoSizeColumns(sheet);
    }

    public static void writeListUserToTemplate(Workbook workbook, int index, List<UserDTO.UserResponseDTO> users, int columnCount) {
        Sheet sheet = workbook.getSheetAt(index);
        int rowIndex = 1;
        for (var item : users) {
            Row row = sheet.getRow(rowIndex);
            if (row == null) row = sheet.createRow(rowIndex);
            writeCell(sheet, row, columnCount, item.getUsername());
            writeCell(sheet, row, columnCount + 1, item.getId());
            rowIndex++;
        }
        autoSizeColumns(sheet);
    }

    public static void writeListStatusIssueToTemplate(Workbook workbook, int index, List<StatusIssueEntity> statuses, int columnCount) {
        Sheet sheet = workbook.getSheetAt(index);
        int rowIndex = 1;
        for (var item : statuses) {
            Row row = sheet.getRow(rowIndex);
            if (row == null) row = sheet.createRow(rowIndex);
            writeCell(sheet, row, columnCount, item.getName());
            writeCell(sheet, row, columnCount + 1, item.getId());
            rowIndex++;
        }
        autoSizeColumns(sheet);
    }

    public static void writeListCategoryToTemplate(Workbook workbook, int index, List<CategoryEntity> categories, int columnCount) {
        Sheet sheet = workbook.getSheetAt(index);
        int rowIndex = 1;
        for (var item : categories) {
            Row row = sheet.getRow(rowIndex);
            if (row == null) row = sheet.createRow(rowIndex);
            writeCell(sheet, row, columnCount, item.getName());
            writeCell(sheet, row, columnCount + 1, item.getId());
            rowIndex++;
        }
        autoSizeColumns(sheet);
    }

    public static void writeListTaskToTemplate(Workbook workbook, int index, List<TaskEntity> tasks, int columnCount) {
        Sheet sheet = workbook.getSheetAt(index);
        int rowIndex = 1;
        for (var item : tasks) {
            Row row = sheet.getRow(rowIndex);
            if (row == null) row = sheet.createRow(rowIndex);
            writeCell(sheet, row, columnCount, item.getSubject());
            writeCell(sheet, row, columnCount + 1, item.getId());
            rowIndex++;
        }
        autoSizeColumns(sheet);
    }
}
