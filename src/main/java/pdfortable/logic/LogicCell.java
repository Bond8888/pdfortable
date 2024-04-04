package pdfortable.logic;

import pdfortable.extend.StrongTextPosition;

import java.util.List;

/**
 * 逻辑单元格
 * @author yanghui
 * @version 1.0
 * @date 2024/3/17 上午9:15
 * @remark 类：
 */
public class LogicCell {

    /**
     * pdf页码
     */
    private int pdfPageNo;
    /**
     * 行索引
     */
    private int rowIndex;
    /**
     * 列索引
     */
    private int cellIndex;
    /**
     * 单元格x坐标
     */
    private int x;
    /**
     * 单元格y坐标
     */
    private int y;
    /**
     * 单元格宽度
     */
    private int width;
    /**
     * 单元格高度
     */
    private int height;
    private String text;
    /**
     * 单元格包含的文本
     */
    private List<StrongTextPosition> strongTextPositionList;


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

    public int getCellIndex() {
        return cellIndex;
    }

    public void setCellIndex(int cellIndex) {
        this.cellIndex = cellIndex;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<StrongTextPosition> getStrongTextPositionList() {
        return strongTextPositionList;
    }

    public void setStrongTextPositionList(List<StrongTextPosition> strongTextPositionList) {
        this.strongTextPositionList = strongTextPositionList;
    }
}
