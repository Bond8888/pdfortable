package pdfortable.logic;

import pdfortable.line.HorizontalLine;
import pdfortable.line.VerticalLine;

import java.util.List;

/**
 * @author yanghui
 * @version 1.0
 * @date 2024/3/17 上午9:34
 * @remark 类：
 */

public class LogicPage {
    private int pageNo;
    private float pdfPageWidth;
    private float pdfPageHeight;

    private int imgWidth;
    private int imgHeight;

    private List<HorizontalLine> horizontalLines;
    private List<VerticalLine> verticalLines;

    private List<LogicRow> logicRowList;

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public float getPdfPageWidth() {
        return pdfPageWidth;
    }

    public void setPdfPageWidth(float pdfPageWidth) {
        this.pdfPageWidth = pdfPageWidth;
    }

    public float getPdfPageHeight() {
        return pdfPageHeight;
    }

    public void setPdfPageHeight(float pdfPageHeight) {
        this.pdfPageHeight = pdfPageHeight;
    }

    public int getImgWidth() {
        return imgWidth;
    }

    public void setImgWidth(int imgWidth) {
        this.imgWidth = imgWidth;
    }

    public int getImgHeight() {
        return imgHeight;
    }

    public void setImgHeight(int imgHeight) {
        this.imgHeight = imgHeight;
    }

    public List<HorizontalLine> getHorizontalLines() {
        return horizontalLines;
    }

    public void setHorizontalLines(List<HorizontalLine> horizontalLines) {
        this.horizontalLines = horizontalLines;
    }

    public List<VerticalLine> getVerticalLines() {
        return verticalLines;
    }

    public void setVerticalLines(List<VerticalLine> verticalLines) {
        this.verticalLines = verticalLines;
    }

    public List<LogicRow> getLogicRowList() {
        return logicRowList;
    }

    public void setLogicRowList(List<LogicRow> logicRowList) {
        this.logicRowList = logicRowList;
    }
}
