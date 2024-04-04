package pdfortable.extend;

/**
 * 抽取pdf文本基本信息
 * @author yanghui
 * @version 1.0
 * @date 2024/3/17 上午9:06
 * @remark 类：
 */
public class StrongTextPosition {

    private int sequence;
    private int pageNo;
    private String text;
    //private List<TextPosition> textPositionList;
    private float x;
    private float y;
    private float width;
    private float height;

    private float pageHeight;
    private float pageWidth;
    private float mergePageHeight;

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getPageHeight() {
        return pageHeight;
    }

    public void setPageHeight(float pageHeight) {
        this.pageHeight = pageHeight;
    }

    public float getPageWidth() {
        return pageWidth;
    }

    public void setPageWidth(float pageWidth) {
        this.pageWidth = pageWidth;
    }

    public float getMergePageHeight() {
        return mergePageHeight;
    }

    public void setMergePageHeight(float mergePageHeight) {
        this.mergePageHeight = mergePageHeight;
    }
}
