package pdfortable.extend;

import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 增强的文本剥离
 *
 * @author yanghui
 * @version 1.0
 * @date 2024/3/17 上午9:39
 * @remark 类：
 */
public class StrongTextExtract extends PDFTextStripper {

    public StrongTextExtract(int pageNoBegin, int pageNoEnd) {
        this.pageNoBegin = pageNoBegin;
        this.pageNoEnd = pageNoEnd;
    }

    private List<StrongTextPosition> strongTextPositions = new ArrayList<>();
    private int pageNoBegin;
    private int pageNoEnd;
    private int sequence = 0;
    private int pageNo = -1;
    private float pageHeight = 0;
    private float pageWidth = 0;
    private float mergePageHeight = 0;

    public List<StrongTextPosition> getStrongTextPositions() {
        return strongTextPositions;
    }

    @Override
    protected void writeString(String text, List<TextPosition> textPositions) throws IOException {
        if (getCurrentPageNo() < pageNoBegin || getCurrentPageNo() > pageNoEnd) {
            return;
        }
        //换页时计算页面高度和累计合并页面高度
        if (getCurrentPageNo() > pageNo) {
            mergePageHeight += pageHeight;
            PDPage page = getCurrentPage();
            PDRectangle mediaBox = page.getMediaBox();
            pageHeight = mediaBox.getHeight();
            pageWidth = mediaBox.getWidth();
            if (pageNo == -1) {
                mergePageHeight = 0;
            }
            pageNo = getCurrentPageNo();
        }

        TextPosition first = textPositions.get(0);
        float maxHeight = first.getHeight();
        float totalWidth = 0;
        for (TextPosition tp : textPositions) {
            maxHeight = Math.max(maxHeight, tp.getHeight());
            totalWidth += tp.getWidthDirAdj();
        }

        StrongTextPosition strongTextPosition = new StrongTextPosition();
        strongTextPosition.setPageNo(getCurrentPageNo());
        strongTextPosition.setPageHeight(pageHeight);
        strongTextPosition.setPageWidth(pageWidth);
        strongTextPosition.setMergePageHeight(mergePageHeight);
        strongTextPosition.setSequence(sequence);
        strongTextPosition.setX(first.getXDirAdj());
        strongTextPosition.setY(first.getYDirAdj());
        strongTextPosition.setWidth(totalWidth);
        strongTextPosition.setHeight(maxHeight);
        strongTextPosition.setText(text);
        strongTextPositions.add(strongTextPosition);
        sequence++;
    }

}
