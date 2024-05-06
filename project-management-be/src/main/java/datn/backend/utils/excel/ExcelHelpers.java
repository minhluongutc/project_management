package datn.backend.utils.excel;

import datn.backend.utils.Constants;
import datn.backend.utils.Utils;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ooxml.POIXMLDocumentPart;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.openxml4j.opc.PackagePartName;
import org.apache.poi.openxml4j.opc.PackagingURIHelper;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.ImageUtils;
import org.apache.poi.xssf.usermodel.XSSFPictureData;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xmlbeans.XmlObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;
import static org.apache.poi.ooxml.POIXMLTypeLoader.DEFAULT_XML_OPTIONS;

public class ExcelHelpers {
  private static final Logger logger = LoggerFactory.getLogger(ExcelHelpers.class);
  private static final String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
  private static final String VN_FULL_DATE = "dd/MM/yyyy";

  public static boolean hasExcelFormat(MultipartFile file) {
    return TYPE.equals(file.getContentType());
  }

  // Get Workbook
  protected static Workbook getWorkbook(InputStream inputStream, String excelFilePath) throws IOException {
    Workbook workbook = null;
    if (excelFilePath.endsWith("xlsx")) {
      workbook = new XSSFWorkbook(inputStream);
    } else if (excelFilePath.endsWith("xls")) {
      workbook = new HSSFWorkbook(inputStream);
    } else {
      throw new IllegalArgumentException("The specified file is not Excel file");
    }

    return workbook;
  }

  // Get cell value
  protected static Object getCellValue(Cell cell) {
    CellType cellType = cell.getCellType();
    Object cellValue = null;
    switch (cellType) {
      case BOOLEAN:
        cellValue = cell.getBooleanCellValue();
        break;
      case FORMULA:
//                Workbook workbook = cell.getSheet().getWorkbook();
//                FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
//                cellValue = evaluator.evaluate(cell).getNumberValue();
        switch (cell.getCachedFormulaResultType()) {
          case BOOLEAN:
            cellValue = cell.getBooleanCellValue();
            break;
          case NUMERIC:
            cellValue = cell.getNumericCellValue();
            break;
          case STRING:
            cellValue = cell.getRichStringCellValue();
            break;
        }
        break;
      case NUMERIC:
        cellValue = cell.getNumericCellValue();
        break;
      case STRING:
        cellValue = cell.getStringCellValue().trim();
        break;
      case _NONE:
      case BLANK:
      case ERROR:
        break;
      default:
        break;
    }

    return cellValue;
  }

  // Write header with format
  protected static void writeHeader(Sheet sheet, int rowIndex, String[] headers) {
    // create CellStyle
    CellStyle cellStyle = createStyleForHeader(sheet);

    // Create row
    Row row = sheet.createRow(rowIndex);

    for (int i = 0; i < headers.length; i++) {
      Cell cell = row.createCell(i);
      cell.setCellStyle(cellStyle);
      cell.setCellValue(headers[i]);
    }
  }

  protected static void writeHeaderError(Sheet sheet, int rowIndex, String[] headers) {
    // create CellStyle
    CellStyle cellStyle = createStyleForHeader(sheet);
    CellStyle cellStyleerr = createStyleForHeader(sheet);
    // Create row
    Row row = sheet.createRow(rowIndex);

    for (int i = 0; i < headers.length; i++) {
      Cell cell = row.createCell(i);
      cell.setCellStyle(cellStyle);
      cell.setCellValue(headers[i]);
    }
    Cell cell = row.createCell(headers.length);
    cellStyleerr.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
    cell.setCellStyle(cellStyleerr);
    cell.setCellValue("Dữ liệu không hợp lệ");

  }

  protected static void writeHeaderCaseError(Sheet sheet, int rowIndex, String[] headers) {
    // create CellStyle
    CellStyle cellStyle = createStyleForHeader(sheet);
    CellStyle cellStyleerr = createStyleForHeader(sheet);
    // Create row
    Row row = sheet.createRow(rowIndex);

    for (int i = 0; i < headers.length; i++) {
      Cell cell = row.createCell(i);
      cell.setCellStyle(cellStyle);
      cell.setCellValue(headers[i]);
    }
    Cell cell = row.createCell(headers.length);
    cellStyleerr.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
    cell.setCellStyle(cellStyleerr);
    cell.setCellValue("Diễn giải");

  }

  // Write to cell
  protected static void writeCell(Sheet sheet, Row row, int columnCount, Object value) {
    // create CellStyle
    //CellStyle cellStyle = createStyleForRow(sheet);
//TODO tạm thời bỏ để pass 64000 cells
        /*if (cellStyleFormatNumber == null) {
            // Format number
            short format = (short) BuiltinFormats.getBuiltinFormat("#,##0");
            // DataFormat df = workbook.createDataFormat();
            // short format = df.getFormat("#,##0");

            //Create CellStyle
            Workbook workbook = row.getSheet().getWorkbook();
            cellStyleFormatNumber = workbook.createCellStyle();
            cellStyleFormatNumber.setDataFormat(format);
        }*/

    //Cell cell = row.createCell(columnCount);
    Cell cell = row.getCell(columnCount);
    if (cell == null) {
      cell = row.createCell(columnCount);
    }
    if (value instanceof Integer) {
      cell.setCellValue((Integer) value);
    } else if (value instanceof Boolean) {
      cell.setCellValue((Boolean) value);
    } else if (value instanceof Timestamp) {
      var data = new SimpleDateFormat(VN_FULL_DATE).format(value);
      cell.setCellValue(data);
    } else if (value instanceof Double) {
      var t = ((Number) value).doubleValue();
      cell.setCellValue(String.valueOf(((Number) value).doubleValue()));
    } else if (value instanceof Long) {
      cell.setCellValue(String.valueOf(((Number) value).longValue()));
    } else {
      cell.setCellValue((String) value);
    }
    //cell.setCellStyle(cellStyle);
  }

  protected static void writeCell2(Sheet sheet, Row row, int columnCount, Object value, boolean isError) {
    // create CellStyle
    Workbook workbook = row.getSheet().getWorkbook();
    CellStyle cellStyle;

    if (isError)
      cellStyle = createStyleForRowError(sheet);
    else
      cellStyle = createStyleForRow(sheet);

    Cell cell = row.createCell(columnCount);
    if (value instanceof Integer) {
      cell.setCellValue((Integer) value);
    } else if (value instanceof Boolean) {
      cell.setCellValue((Boolean) value);
    } else if (value instanceof Timestamp) {
      var data = new SimpleDateFormat(VN_FULL_DATE).format(value);
      cell.setCellValue(data);
    } else if (value instanceof Double) {
      var t = ((Number) value).doubleValue();
      cell.setCellValue(String.valueOf(((Number) value).doubleValue()));
    } else if (value instanceof Long) {
      cell.setCellValue(String.valueOf(((Number) value).longValue()));
    } else {
      cell.setCellValue((String) value);
    }
    cell.setCellStyle(cellStyle);
  }

  // Create CellStyle for header
  protected static CellStyle createStyleForHeader(Sheet sheet) {
    // Create font
    Font font = sheet.getWorkbook().createFont();
    font.setFontName("Calibri");
    font.setBold(true);
    font.setFontHeightInPoints((short) 14); // font size
    font.setColor(IndexedColors.BLACK.getIndex()); // text color

    // Create CellStyle
    CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
    cellStyle.setFont(font);
    cellStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
    cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    cellStyle.setBorderBottom(BorderStyle.THIN);
    cellStyle.setBorderRight(BorderStyle.THIN);
    return cellStyle;
  }

  // Create CellStyle for note
  protected static CellStyle createStyleForNote(Sheet sheet) {
    // Create font
    Font font = sheet.getWorkbook().createFont();
    font.setFontName("Calibri");
    font.setBold(false);
    font.setItalic(true);
    font.setFontHeightInPoints((short) 12); // font size
    font.setColor(IndexedColors.BLACK.getIndex()); // text color

    // Create CellStyle
    CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
    cellStyle.setFont(font);
    cellStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
    cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    cellStyle.setBorderBottom(BorderStyle.DOUBLE);
    cellStyle.setBorderRight(BorderStyle.THIN);
    return cellStyle;
  }

  // Create CellStyle for header
  protected static CellStyle createStyleForRow(Sheet sheet) {
    // Create font
    Font font = sheet.getWorkbook().createFont();
    font.setFontName("Calibri");
    font.setBold(false);
    font.setFontHeightInPoints((short) 14); // font size
    font.setColor(IndexedColors.BLACK.getIndex()); // text color

    // Create CellStyle
    CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
    cellStyle.setFont(font);
    cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
    cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    cellStyle.setBorderBottom(BorderStyle.THIN);
    cellStyle.setBorderRight(BorderStyle.THIN);
    return cellStyle;
  }

  protected static CellStyle createStyleForRowError(Sheet sheet) {
    // Create font
    Font font = sheet.getWorkbook().createFont();
    font.setFontName("Calibri");
    font.setBold(false);
    font.setFontHeightInPoints((short) 14); // font size
    font.setColor(IndexedColors.BLACK.getIndex()); // text color

    // Create CellStyle
    CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
    cellStyle.setFont(font);
    cellStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
    cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    cellStyle.setBorderBottom(BorderStyle.THIN);
    cellStyle.setBorderRight(BorderStyle.THIN);
    return cellStyle;
  }

  public static void autoSizeColumns(Sheet sheet) {
    if (sheet.getPhysicalNumberOfRows() > 0) {
      Row row = sheet.getRow(sheet.getFirstRowNum());
      Iterator<Cell> cellIterator = row.cellIterator();
      while (cellIterator.hasNext()) {
        Cell cell = cellIterator.next();
        int columnIndex = cell.getColumnIndex();
        sheet.autoSizeColumn(columnIndex);
      }
    }
  }

  // Create output file
  protected static void createOutputFile(Workbook workbook, String folder, String excelFilePath) throws IOException {
    //Tạo thư mục chứa ảnh nếu không tồn tại
    File uploadDir = new File(folder);
    if (!uploadDir.exists()) {
      uploadDir.mkdirs();
    }
    File fileExcel = new File(folder + excelFilePath);
    if (!fileExcel.createNewFile()) {
      logger.error("createOutputFile file is already exist, Override!");
    }
    try (OutputStream os = new FileOutputStream(fileExcel)) {
      workbook.write(os);
    }
  }

  // Export file not create file on server
  protected static void exportStream(Workbook workbook, HttpServletResponse response) throws IOException {
    ServletOutputStream outputStream = response.getOutputStream();
    workbook.write(outputStream);
    workbook.close();
    outputStream.close();
  }

  protected static boolean everyCellBlank(List<Object> list) {
    if (list.size() == 0) // để demo
      return true;
    else
      return list.stream().allMatch(ExcelHelpers::isBlank);
  }

  protected static boolean everyCellNotBlank(List<Object> list) {
//        return true;//TODO check lại quy tắc kiểm tra null sau
    if (list.size() == 0) // để demo
      return true;
    else
      return list.stream().allMatch(ExcelHelpers::isNotBlank);
  }

  protected static boolean someCellBlank(List<Object> list) {
//        return false;//TODO check lại quy tắc kiểm tra null sau
    if (list.size() == 0) // để demo
      return true;
    else
      return list.stream().anyMatch(ExcelHelpers::isBlank);
  }

  protected static boolean someCellNotBlank(List<Object> list) {
//        return false;//TODO check lại quy tắc kiểm tra null sau
    if (list.size() == 0) // để demo
      return true;
    else
      return list.stream().anyMatch(ExcelHelpers::isNotBlank);
  }

  protected static BufferedImage drawText(String text, java.awt.Font font, java.awt.Color textColor, java.awt.Color backColor, double height, double width) {
    //define the width and height of image
    BufferedImage img = new BufferedImage((int) width, (int) height, TYPE_INT_ARGB);
    Graphics2D loGraphic = img.createGraphics();

    //set the font size
    FontMetrics loFontMetrics = loGraphic.getFontMetrics(font);
    int liStrWidth = loFontMetrics.stringWidth(text);
    int liStrHeight = loFontMetrics.getHeight();
    //set the text format
    loGraphic.setColor(backColor);
    loGraphic.fillRect(0, 0, (int) width, (int) height);
    loGraphic.translate(((int) width - liStrWidth) / 2, ((int) height - liStrHeight) / 2);
    loGraphic.rotate(Math.toRadians(-45));
    loGraphic.translate(-((int) width - liStrWidth) / 2, -((int) height - liStrHeight) / 2);
    loGraphic.setFont(font);
    loGraphic.setColor(textColor);
    loGraphic.drawString(text, ((int) width - liStrWidth) / 2, ((int) height - liStrHeight) / 2);
    loGraphic.dispose();
    return img;
  }

  protected static void createPictureForHeader(XSSFSheet sheet,
                                               int pictureIdx,
                                               String pictureTitle,
                                               int vmlIdx,
                                               String headerPos) throws Exception {
    OPCPackage opcpackage = sheet.getWorkbook().getPackage();

    //creating /xl/drawings/vmlDrawing1.vml
    PackagePartName partname = PackagingURIHelper.createPartName("/xl/drawings/vmlDrawing" + vmlIdx + ".vml");
    PackagePart part = opcpackage.createPart(partname, "application/vnd.openxmlformats-officedocument.vmlDrawing");
    //creating new VmlDrawing
    VmlDrawing vmldrawing = new VmlDrawing(part);

    //creating the relation to the picture in /xl/drawings/_rels/vmlDrawing1.vml.rels
    XSSFPictureData picData = sheet.getWorkbook().getAllPictures().get(pictureIdx);
    String rIdPic = vmldrawing.addRelation(null, XSSFRelation.IMAGES, picData).getRelationship().getId();

    //get image dimension
    ByteArrayInputStream is = new ByteArrayInputStream(picData.getData());
    Dimension imageDimension = ImageUtils.getImageDimension(is, picData.getPictureType());
    is.close();

    //updating the VmlDrawing
    vmldrawing.setRIdPic(rIdPic);
    vmldrawing.setPictureTitle(pictureTitle);
    vmldrawing.setImageDimension(imageDimension);
    vmldrawing.setHeaderPos(headerPos);

    //creating the relation to /xl/drawings/vmlDrawing1.xml in /xl/worksheets/_rels/sheet1.xml.rels
    String rIdExtLink = sheet.addRelation(null, XSSFRelation.VML_DRAWINGS, vmldrawing).getRelationship().getId();

    //creating the <legacyDrawingHF r:id="..."/> in /xl/worksheets/sheetN.xml
    sheet.getCTWorksheet().addNewLegacyDrawingHF().setId(rIdExtLink);

  }

  //class for VmlDrawing
  protected static class VmlDrawing extends POIXMLDocumentPart {

    String rIdPic = "";
    String pictureTitle = "";
    Dimension imageDimension = null;
    String headerPos = "";

    VmlDrawing(PackagePart part) {
      super(part);
    }

    void setRIdPic(String rIdPic) {
      this.rIdPic = rIdPic;
    }

    void setPictureTitle(String pictureTitle) {
      this.pictureTitle = pictureTitle;
    }

    void setHeaderPos(String headerPos) {
      this.headerPos = headerPos;
    }

    void setImageDimension(Dimension imageDimension) {
      this.imageDimension = imageDimension;
    }

    @Override
    protected void commit() throws IOException {
      PackagePart part = getPackagePart();
      OutputStream out = part.getOutputStream();
      try {
        XmlObject doc = XmlObject.Factory.parse(

          "<xml xmlns:v=\"urn:schemas-microsoft-com:vml\""
            + " xmlns:o=\"urn:schemas-microsoft-com:office:office\""
            + " xmlns:x=\"urn:schemas-microsoft-com:office:excel\">"
            + " <o:shapelayout v:ext=\"edit\">"
            + "  <o:idmap v:ext=\"edit\" data=\"1\"/>"
            + " </o:shapelayout><v:shapetype id=\"_x0000_t75\" coordsize=\"21600,21600\" o:spt=\"75\""
            + "  o:preferrelative=\"t\" path=\"m@4@5l@4@11@9@11@9@5xe\" filled=\"f\" stroked=\"f\">"
            + "  <v:stroke joinstyle=\"miter\"/>"
            + "  <v:formulas>"
            + "   <v:f eqn=\"if lineDrawn pixelLineWidth 0\"/>"
            + "   <v:f eqn=\"sum @0 1 0\"/>"
            + "   <v:f eqn=\"sum 0 0 @1\"/>"
            + "   <v:f eqn=\"prod @2 1 2\"/>"
            + "   <v:f eqn=\"prod @3 21600 pixelWidth\"/>"
            + "   <v:f eqn=\"prod @3 21600 pixelHeight\"/>"
            + "   <v:f eqn=\"sum @0 0 1\"/>"
            + "   <v:f eqn=\"prod @6 1 2\"/>"
            + "   <v:f eqn=\"prod @7 21600 pixelWidth\"/>"
            + "   <v:f eqn=\"sum @8 21600 0\"/>"
            + "   <v:f eqn=\"prod @7 21600 pixelHeight\"/>"
            + "   <v:f eqn=\"sum @10 21600 0\"/>"
            + "  </v:formulas>"
            + "  <v:path o:extrusionok=\"f\" gradientshapeok=\"t\" o:connecttype=\"rect\"/>"
            + "  <o:lock v:ext=\"edit\" aspectratio=\"t\"/>"
            + " </v:shapetype><v:shape id=\"" + headerPos + "\" o:spid=\"_x0000_s1025\" type=\"#_x0000_t75\""
            + "  style='position:absolute;margin-left:0;margin-top:0;"
            + "width:" + (int) imageDimension.getWidth() + "px;height:" + (int) imageDimension.getHeight() + "px;"
            + "z-index:1'>"
            + "  <v:imagedata o:relid=\"" + rIdPic + "\" o:title=\"" + pictureTitle + "\"/>"
            + "  <o:lock v:ext=\"edit\" rotation=\"t\"/>"
            + " </v:shape></xml>"

        );
        doc.save(out, DEFAULT_XML_OPTIONS);
        out.close();
      } catch (Exception ex) {
        logger.error("ExcelHelper: ", ex);
      }
    }
  }

  public static boolean isBlank(Object obj) {
    return obj == null || StringUtils.isBlank(obj.toString());
  }

  public static boolean isNotBlank(Object obj) {
    return !isBlank(obj);
  }


  /**
   * check content row has empty or null
   *
   * @param row      Du lieu ma row can kiem tra
   * @param fromCell Cot bat dau thuc hien
   * @param toCell   Cot Ket thuc thuc hien
   * @return Chuoi kiem tra
   */
  public static boolean hasEmptyAllCellOnRow(Row row, int fromCell, int toCell) {
    if (row != null) {
      for (int i = fromCell; i <= toCell; i++) {
        if (!StringUtils.isEmpty(getStringCellValue(row.getCell(i)))) {
          return true;
        }
      }
    }
    return false;
  }

  public static String getStringCellValue(Cell cell) {
    try {
      if (cell == null) {
        return "";
      } else if (CellType.BLANK == cell.getCellType()) {
        return "";
      } else if (CellType.BOOLEAN == cell.getCellType()) {
        return cell.getBooleanCellValue() + "";
      } else if (CellType.ERROR == cell.getCellType()) {
        return null;
      } else if (CellType.FORMULA == cell.getCellType()) {
        switch (cell.getCachedFormulaResultType()) {
          case BOOLEAN:
            return cell.getBooleanCellValue() + "";
          case NUMERIC:
            return String.format("%.0f", cell.getNumericCellValue());
          case STRING:
            return StringUtils.trim(cell.getStringCellValue());
        }
      } else if (CellType.NUMERIC == cell.getCellType()) {
        if (DateUtil.isCellDateFormatted(cell)) {
          return Utils.formatter.format(cell.getDateCellValue());
        } else {
          return String.format("%.0f", cell.getNumericCellValue());
        }
      } else if (CellType.STRING == cell.getCellType()) {
        return StringUtils.trim(cell.getStringCellValue());
      }
    } catch (Exception e) {
      logger.error("Error when cast value to String", e);
    }
    return "";
  }

  public static Date cellValue2Date(Cell cell) throws ParseException {
    return Utils.formatter.parse(ExcelHelpers.getStringCellValue(cell));
  }

  public static boolean rowsAreEqual(Row template, Row input) {
    int equalCount = 0;
    for (int i = 0; i < template.getLastCellNum(); i++) {
      Cell c1 = template.getCell(i);
      Cell c2 = input.getCell(i);

      String s1 = getStringCellValue(c1);
      String s2 = getStringCellValue(c2);

      if (s1 != null && s1.equals(s2)) {
        equalCount++;
      }
    }

    return equalCount == template.getLastCellNum();
  }


}
