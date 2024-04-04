# 主要功能
1.PDF转Excel
2.抽取PDF表格数据
3.可按页码范围转换Excel或抽取表格数据

# 使用示例
```
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
```
