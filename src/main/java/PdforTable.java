import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import pdfortable.extend.StrongTextExtract;
import pdfortable.extend.StrongTextPosition;
import pdfortable.line.HorizontalLine;
import pdfortable.line.VerticalLine;
import pdfortable.logic.LogicCell;
import pdfortable.logic.LogicPage;
import pdfortable.logic.LogicRow;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 1.PDF转Exceel
 * 2.抽取PDF中表格数据
 * 3.按指定页码范围转换Excel
 * 4.按指定页码范围抽取PDF中表格数据
 * @author yanghui
 * @version 1.0
 * @date 2024/3/17 上午9:05
 * @remark 类：
 */
public class PdforTable {

    public static void main(String[] args) {
        String pdfPath = "/Users/trans.pdf";
        String excelPath = "/Users/export.xls";

        PdforTable pdforTable = new PdforTable();

        //示例1：抽取PDF中的表格数据
        LogicPage logicPage = pdforTable.extractTable(pdfPath);
        //LogicPage logicPage = pdforTable.extractTable(pdfPath,1,2);
        logicPage.getLogicRowList().forEach(logicRow -> {
            System.out.println("第" + logicRow.getRowIndex() + "行");
            System.out.print("[");
            logicRow.getLogicCellList().forEach(logicCell -> {
                System.out.print(logicCell.getText() + ",");
            });
            System.out.println("]");
        });

        //示例2：转换PDF为Excel
        pdforTable.convertPdfToExcel(pdfPath, excelPath);
        //pdforTable.convertPdfToExcel(pdfPath, excelPath, 2, 4);

    }

    // 图片DPI
    private static final int DPI_IMAGE = 300;
    // PDF DPI
    private static final int DPI_PDF = 72;
    //二值化阈值
    private static final int threshold = 128;
    // 水平线段，垂直线段检测连续像素数
    private static final int horizontalThreshold = 200;
    private static final int verticalThreshold = 100;

    /**
     * 转换PDF为Excel
     *
     * @param pdfPath   PDF文件路径
     * @param excelPath 输出excel文件路径
     */
    public void convertPdfToExcel(String pdfPath, String excelPath) {
        convertPdfToExcel(pdfPath, excelPath, null, null);
    }

    /**
     * 转换PDF为Excel
     *
     * @param pdfPath     PDF文件路径
     * @param excelPath   输出excel文件路径
     * @param pageNoBegin pdf起始页码，从1开始计算
     * @param pageNoEnd   pdf截止页码，从1开始计算
     */
    public void convertPdfToExcel(String pdfPath, String excelPath, Integer pageNoBegin, Integer pageNoEnd) {

        LogicPage logicPage = extractTable(pdfPath, pageNoBegin, pageNoEnd);
        if (logicPage != null) {
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet();
            HSSFCellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setBorderTop(BorderStyle.THIN);
            cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
            cellStyle.setBorderLeft(BorderStyle.THIN);
            cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
            cellStyle.setBorderRight(BorderStyle.THIN);
            cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());

            logicPage.getLogicRowList().forEach(logicRow -> {
                Row row = sheet.createRow(logicRow.getRowIndex());
                logicRow.getLogicCellList().forEach(logicCell -> {
                    Cell cell = row.createCell(logicCell.getCellIndex());
                    cell.setCellValue(logicCell.getText());
                    cell.setCellStyle(cellStyle);
                });
            });
            try (FileOutputStream out = new FileOutputStream(excelPath)) {
                workbook.write(out);
                workbook.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 从PDF文件中抽取表格数据
     *
     * @param pdfPath     PDF文件路径
     * @param pageNoBegin pdf起始页码，从1开始计算
     * @param pageNoEnd   pdf截止页码，从1开始计算
     */
    public LogicPage extractTable(String pdfPath, Integer pageNoBegin, Integer pageNoEnd) {
        try (PDDocument document = Loader.loadPDF(new File(pdfPath))) {
            AccessPermission ap = document.getCurrentAccessPermission();
            if (!ap.canExtractContent()) {
                throw new IOException("PDF文档权限不足");
            }
            if (pageNoBegin == null) {
                pageNoBegin = 1;
            }
            if (pageNoEnd == null) {
                pageNoEnd = document.getNumberOfPages();
            }
            if (pageNoBegin < 1 || pageNoBegin > document.getNumberOfPages()) {
                throw new RuntimeException("提取页码不正确！请设置 pageNoBegin 1-" + document.getNumberOfPages() + "之间");
            }
            if (pageNoEnd < pageNoBegin || pageNoEnd > document.getNumberOfPages()) {
                throw new RuntimeException("提取页码不正确！请设置 pageNoEnd " + pageNoBegin + "-" + document.getNumberOfPages() + "之间");
            }

            StrongTextExtract stripper = new StrongTextExtract(pageNoBegin, pageNoEnd);
            stripper.getText(document);
            //按列排序，这里非常重要
            stripper.setSortByPosition(false);
            List<StrongTextPosition> strongTextPositions = stripper.getStrongTextPositions();

            List<BufferedImage> pdfImgPages = new ArrayList<>();

            for (int p = pageNoBegin - 1; p < pageNoEnd; p++) {
                //转换图片
                PDFRenderer pdfRenderer = new PDFRenderer(document);
                BufferedImage originalImage = pdfRenderer.renderImageWithDPI(p, DPI_IMAGE);
                pdfImgPages.add(originalImage);
            }
            //合并图片
            BufferedImage mergeImg = mergeImagesVertically(pdfImgPages);

            //图片二值化
            BufferedImage binaryImage = imgBinarized(mergeImg, threshold);

            LogicPage logicPage = new LogicPage();
            logicPage.setPageNo(0);
            //识别行与列
            searchLine(binaryImage, logicPage, horizontalThreshold, verticalThreshold);

            List<HorizontalLine> horizontalLines = logicPage.getHorizontalLines();
            List<VerticalLine> verticalLines = logicPage.getVerticalLines();
            //按y轴坐标排序，相同y坐标按x排序(同行多线段)
            Map<Integer, List<HorizontalLine>> sortedHorizon = horizontalLines.stream()
                    .sorted(Comparator.comparingInt(HorizontalLine::getY)
                            .thenComparingInt(HorizontalLine::getX))
                    .collect(Collectors.groupingBy(HorizontalLine::getY, LinkedHashMap::new, Collectors.toList()));

            //按x轴坐标排序，相同x坐标按y排序(同列多线段)
            Map<Integer, List<VerticalLine>> sortedVertical = verticalLines.stream()
                    .sorted(Comparator.comparingInt(VerticalLine::getX)
                            .thenComparingInt(VerticalLine::getY))
                    .collect(Collectors.groupingBy(VerticalLine::getX,
                            LinkedHashMap::new, Collectors.toList()));

            List<Map.Entry<Integer, List<HorizontalLine>>> HorizontalEntryList = new ArrayList<>(sortedHorizon.entrySet());
            List<Map.Entry<Integer, List<VerticalLine>>> verticalEntryList = new ArrayList<>(sortedVertical.entrySet());

            logicPage.setLogicRowList(new ArrayList<>());

            for (int i = 0; i < HorizontalEntryList.size() - 1; i++) {
                //一行中可能有多段线段（合并单元格导致多段）
                //上线
                Map.Entry<Integer, List<HorizontalLine>> topLineEntry = HorizontalEntryList.get(i);
                //底线
                Map.Entry<Integer, List<HorizontalLine>> bottomLineEntry = HorizontalEntryList.get(i + 1);
                List<HorizontalLine> topLines = topLineEntry.getValue();
                List<HorizontalLine> bottomLines = bottomLineEntry.getValue();
                HorizontalLine topLeft = topLines.get(0);
                HorizontalLine topRight = topLines.get(topLines.size() - 1);
                HorizontalLine bottomLeft = bottomLines.get(0);
                HorizontalLine bottomRight = bottomLines.get(bottomLines.size() - 1);

                //行top横向x坐标
                int topMinX = topLeft.getX();
                int topMaxX = topRight.getX() + topRight.getLength();
                int topWidth = topMaxX - topMinX;
                //行底线横向x坐标
                int bottomMinX = bottomLeft.getX();
                int bottomMaxX = bottomRight.getX() + bottomRight.getLength();
                int bottomWidth = bottomMaxX - bottomMinX;
                //行的y坐标区域
                int topY = topLineEntry.getKey();
                int bottomY = bottomLineEntry.getKey();
                //计算行宽度，行高度
                int height = bottomY - topY;
                int width = Math.max(topWidth, bottomWidth);

                LogicRow logicRow = new LogicRow();
                logicRow.setPdfPageNo(logicPage.getPageNo());
                logicRow.setRowIndex(logicPage.getLogicRowList().size());
                logicRow.setX(Math.min(topMinX, bottomMinX));
                logicRow.setY(topY);
                logicRow.setHeight(height);
                logicRow.setWidth(width);
                logicRow.setLogicCellList(new ArrayList<>());
                logicPage.getLogicRowList().add(logicRow);
                // 遍历垂直线，计算LogicCell
                for (int j = 0; j < verticalEntryList.size() - 1; j++) {
                    Map.Entry<Integer, List<VerticalLine>> leftLineEntry = verticalEntryList.get(j);
                    Map.Entry<Integer, List<VerticalLine>> rightLineEntry = verticalEntryList.get(j + 1);
                    //行的x区域
                    int leftX = leftLineEntry.getKey();
                    int rightX = rightLineEntry.getKey();
                    int cellWidth = rightX - leftX;
                    //图片换算PDF：
                    // pdfWdth = imgWidth / (imgDPI / pdfPDI)
                    float pdfW = cellWidth / ((float) DPI_IMAGE / (float) DPI_PDF);
                    float pdfH = height / ((float) DPI_IMAGE / (float) DPI_PDF);
                    float pdfX = leftX / ((float) DPI_IMAGE / (float) DPI_PDF);
                    float pdfY = topY / ((float) DPI_IMAGE / (float) DPI_PDF);
                    LogicCell logicCell = new LogicCell();
                    logicRow.getLogicCellList().add(logicCell);
                    logicCell.setPdfPageNo(logicPage.getPageNo());
                    logicCell.setRowIndex(logicRow.getRowIndex());
                    logicCell.setCellIndex(j);
                    logicCell.setX(leftX);
                    logicCell.setY(topY);
                    logicCell.setHeight(height);
                    logicCell.setWidth(rightX - leftX);
                    logicCell.setStrongTextPositionList(new ArrayList<>());
                    logicCell.setText("");
                    //查找文本
                    strongTextPositions.forEach(textPosition -> {
                        float textY = textPosition.getMergePageHeight() + textPosition.getY();
                        if (textPosition.getX() >= pdfX && textPosition.getX() <= pdfX + pdfW) {
                            if (textY >= pdfY && textY <= pdfY + pdfH) {
                                logicCell.getStrongTextPositionList().add(textPosition);
                                logicCell.setText(logicCell.getText() + textPosition.getText());
                            }
                        }
                    });
                }
            }
            return logicPage;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 从PDF文件中抽取表格数据
     *
     * @return LogicPage
     */
    public LogicPage extractTable(String pdfPath) {
        return extractTable(pdfPath, null, null);
    }

    /**
     * 合并多个BufferedImage
     *
     * @param imgs
     * @return
     */
    private BufferedImage mergeImagesVertically(List<BufferedImage> imgs) {
        if (imgs == null || imgs.isEmpty()) {
            throw new IllegalArgumentException("Image list cannot be null or empty");
        }
        int width = 0;
        int height = 0;
        for (BufferedImage img : imgs) {
            width = Math.max(width, img.getWidth());
            height += img.getHeight();
        }
        BufferedImage mergedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = mergedImage.createGraphics();
        int currentY = 0;
        for (BufferedImage img : imgs) {
            g2d.drawImage(img, 0, currentY, null);
            currentY += img.getHeight();
        }
        g2d.dispose();
        return mergedImage;
    }

    /**
     * 二值化
     *
     * @param originalImage
     * @param threshold     阈值
     * @return
     */
    private BufferedImage imgBinarized(BufferedImage originalImage, int threshold) {
        BufferedImage grayImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        BufferedImage binaryImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        WritableRaster originalRaster = originalImage.getRaster();
        WritableRaster grayRaster = grayImage.getRaster();
        WritableRaster binaryRaster = binaryImage.getRaster();
        // 灰度图像
        for (int y = 0; y < originalImage.getHeight(); y++) {
            for (int x = 0; x < originalImage.getWidth(); x++) {
                int r = originalRaster.getSample(x, y, 0);
                int g = originalRaster.getSample(x, y, 1);
                int b = originalRaster.getSample(x, y, 2);
                int brightness = (r + g + b) / 3;
                grayRaster.setSample(x, y, 0, brightness);
            }
        }
        // 二值化图像
        for (int y = 0; y < grayImage.getHeight(); y++) {
            for (int x = 0; x < grayImage.getWidth(); x++) {
                int grayValue = grayRaster.getSample(x, y, 0);
                int binaryValue = (grayValue >= threshold) ? 1 : 0;
                binaryRaster.setSample(x, y, 0, binaryValue);
            }
        }
        return binaryImage;
    }

    /**
     * 搜索线段
     *
     * @param binaryImage         经过二值化的图像
     * @param horizontalThreshold 水平线检测阈值
     * @param verticalThreshold   垂直线检测阈值
     */
    private void searchLine(BufferedImage binaryImage, LogicPage logicPage, int horizontalThreshold, int verticalThreshold) {

        List<HorizontalLine> horizontalLines = new ArrayList<>();
        List<VerticalLine> verticalLines = new ArrayList<>();
        WritableRaster binaryRaster = binaryImage.getRaster();
        int width = binaryImage.getWidth();
        int height = binaryImage.getHeight();
        //水平线检测
        for (int y = 0; y < height; y++) {
            int blackPixelCount = 0;
            int lineStart = -1;
            for (int x = 0; x < width; x++) {
                int binaryValue = binaryRaster.getSample(x, y, 0);
                if (binaryValue == 0) {
                    blackPixelCount++;
                    if (lineStart == -1) {
                        lineStart = x;
                    }
                } else {
                    if (blackPixelCount >= horizontalThreshold) {
                        horizontalLines.add(new HorizontalLine(lineStart, y, blackPixelCount));
                    }
                    blackPixelCount = 0;
                    lineStart = -1;
                }
            }
            if (blackPixelCount >= horizontalThreshold) {
                horizontalLines.add(new HorizontalLine(lineStart, y, blackPixelCount));
            }
        }
        //合并y方向连续像素线条
        for (int i = 0; i < horizontalLines.size(); ) {
            HorizontalLine currentLine = horizontalLines.get(i);
            int step = 1;
            for (int j = i + 1; j < horizontalLines.size(); j++) {
                HorizontalLine nextLine = horizontalLines.get(j);
                if (nextLine.getY() == currentLine.getY() + step) {
                    horizontalLines.remove(j);
                    j--;
                    step++;
                }
            }
            i++;
        }
        //垂直线检测
        for (int x = 0; x < width; x++) {
            int blackPixelCount = 0;
            int lineStart = -1;
            for (int y = 0; y < height; y++) {
                int binaryValue = binaryRaster.getSample(x, y, 0);
                if (binaryValue == 0) {
                    blackPixelCount++;
                    if (lineStart == -1) {
                        lineStart = y;
                    }
                } else {
                    if (blackPixelCount >= verticalThreshold) {
                        verticalLines.add(new VerticalLine(x, lineStart, blackPixelCount));
                    }
                    blackPixelCount = 0;
                    lineStart = -1;
                }
            }
            if (blackPixelCount >= verticalThreshold) {
                verticalLines.add(new VerticalLine(x, lineStart, blackPixelCount));
            }
        }

        //合并y方向连续像素线条
        for (int i = 0; i < verticalLines.size(); ) {
            VerticalLine currentLine = verticalLines.get(i);
            int step = 1;
            for (int j = i + 1; j < verticalLines.size(); j++) {
                VerticalLine nextLine = verticalLines.get(j);
                if (nextLine.getX() == currentLine.getX() + step) {
                    verticalLines.remove(j);
                    j--;
                    step++;
                }
            }
            i++;
        }
        logicPage.setHorizontalLines(horizontalLines);
        logicPage.setVerticalLines(verticalLines);

    }
}
