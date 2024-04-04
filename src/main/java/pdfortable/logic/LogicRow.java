package pdfortable.logic;

import java.util.List;

/**
 * 逻辑行
 *
 * @author yanghui
 * @version 1.0
 * @date 2024/3/17 上午9:18
 * @remark 类：
 */

public class LogicRow {

    /**
     * pdf页码
     */
    private int pdfPageNo;
    /**
     * 行索引
     */
    private int rowIndex;

    /**
     * 行x坐标
     */
    private double x;
    /**
     * 行y坐标
     */
    private double y;
    /**
     * 行宽度
     */
    private double width;
    /**
     * 行高度
     */
    private double height;
    /**
     * 包含的逻辑单元格
     */
    private List<LogicCell> logicCellList;

    public int getPdfPageNo() {
        return pdfPageNo;
    }

    public void setPdfPageNo(int pdfPageNo) {
        this.pdfPageNo = pdfPageNo;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public List<LogicCell> getLogicCellList() {
        return logicCellList;
    }

    public void setLogicCellList(List<LogicCell> logicCellList) {
        this.logicCellList = logicCellList;
    }
}
