package pdfortable.line;

/**
 * @author yanghui
 * @version 1.0
 * @date 2024/3/17 上午11:28
 * @remark 类：
 */
public class HorizontalLine {
    public HorizontalLine() {

    }
    public HorizontalLine(int x, int y, int length) {
        this.x = x;
        this.y = y;
        this.length = length;
    }

    private int x;
    private int y;
    private int length;

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

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
