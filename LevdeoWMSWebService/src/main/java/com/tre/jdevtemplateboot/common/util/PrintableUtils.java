package com.tre.jdevtemplateboot.common.util;

import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class PrintableUtils extends JFrame implements Printable {

    //发货单单号
    private String invoiceCode;
    //根据发货单号查询到的head
    private Map<String, Object> headData;
    //打印数据
    private List<Map<String, Object>> dataList;
    //每页显示数据条数
    private int rowsperpage;
    //总页数
    private int pagecnt;

    public PrintableUtils(Map<String, Object> headData, List<Map<String, Object>> dataList, int rowsperpage){
        this.headData = headData;
        this.dataList = dataList;
        this.rowsperpage = rowsperpage;
        this.invoiceCode = StringUtils.defaultIfEmpty((String)headData.get("invoiceCode"), "");
        this.pagecnt = dataList.size()/rowsperpage+1;
    }

    @Override
    public int print(Graphics gra, PageFormat pf, int pageIndex) throws PrinterException {
        String title = "山东梅拉德能源动力科技有限公司";
        String subtitle = "发货通知单";

        //打印起点坐标
        double x = pf.getImageableX();
        double y = pf.getImageableY();
        //打印区域宽度、高度
        double width = pf.getImageableWidth();
        double height = pf.getImageableHeight();

        //title Y坐标
        float titleY = (float)y+30;
        //subtitle Y坐标
        float subtitleY = titleY+15;
        //售达方 坐标
        float x1 = 60;
        float y1 = subtitleY+20;

        //第二行 Y坐标
        float y2 = y1+15;
        //第三行 Y坐标
        float y3 = y2+15;
        //第四行 Y坐标
        float y4 = y3+15;

        //第二列 X坐标
        float x2 = x1+60;
        //第三列 X坐标
        float x3 = x2+250;
        //第四列 X坐标
        float x4 = x3+50;
        //第五列 X坐标
        float x5 = x4+60;
        //第六列 X坐标
        float x6 = x5+50;

        //表格 Y坐标
        int tableY = (int) (y4+15);

        //底线距离表格底部的距离
        int linedis = 50;

        //底线下的字距离底线的距离
        int linetextdis = 15;

        if(pageIndex >= 0){
            //转换成Graphics2D
            Graphics2D g2 = (Graphics2D) gra;
            //设置打印颜色为黑色
            g2.setColor(Color.BLACK);
            //标题字体
            Font titlefont = new Font("新宋体", Font.BOLD, 11);
            //设置字体
            g2.setFont(titlefont);
            //字体高度
            //float fontheight = titlefont.getSize2D();

            FontMetrics fontMetrics = g2.getFontMetrics(titlefont);
            //title宽度
            int titleWidth = fontMetrics.stringWidth(title);
            //subtitle
            int subtitleWidth = fontMetrics.stringWidth(subtitle);

            //-------------画标题、副标题-------------
            g2.drawString(title, (float) (width-titleWidth)/2, titleY);
            g2.drawString(subtitle, (float) (width-subtitleWidth)/2, subtitleY);

            //字体
            Font font = new Font("新宋体", Font.PLAIN, 10);
            //设置字体
            g2.setFont(font);

            //-------------第一列-------------
            g2.drawString("售达方:", x1, y1);
            g2.drawString("送达方地址:", x1, y2);
            g2.drawString("开单日期:", x1, y3);
            g2.drawString("送达方:", x1, y4);

            //-------------第二列-------------
            //售达方
            g2.drawString(StringUtils.defaultIfEmpty((String)headData.get("salecustomName"), ""), x2, y1);
            //送达方地址
            g2.drawString(StringUtils.defaultIfEmpty((String)headData.get("sendcustomAddress"), ""), x2, y2);
            //开单日期
            g2.drawString(StringUtils.defaultIfEmpty((String)headData.get("createDate"), ""), x2, y3);
            //送达方
            g2.drawString(StringUtils.defaultIfEmpty((String)headData.get("sendcustomName"), ""), x2, y4);

            //-------------第三列-------------
            g2.drawString("销售订单:", x3, y1);
            g2.drawString("发货单号:", x3, y2);
            g2.drawString("运货方式:", x3, y3);

            //-------------第四列-------------
            //销售订单
            g2.drawString(StringUtils.defaultIfEmpty((String)headData.get("salesOrder"), ""), x4, y1);
            //发货单号
            g2.drawString(invoiceCode, x4, y2);
            //货运方式
            g2.drawString(StringUtils.defaultIfEmpty((String)headData.get("shipType"), ""), x4, y3);

            //-------------第五列-------------
            g2.drawString("物流车位:", x5, y1);
            g2.drawString("物流车号:", x5, y2);
            g2.drawString("随车物料:", x5, y3);

            //-------------第六列-------------
            //物流车位
            g2.drawString(StringUtils.defaultIfEmpty((String)headData.get("logisticsParking"), ""), x6, y1);
            //物流车号
            g2.drawString(StringUtils.defaultIfEmpty((String)headData.get("logisticsVehicleNo"), ""), x6, y2);
            //随车物料
            g2.drawString(StringUtils.defaultIfEmpty((String)headData.get("giftName"), ""), x6, y3);

            //-------------表格-------------
            //表头
            String[] colnames = {"项目","物料描述","数量","发出仓库","VIN码","存放点","条形码"};
            //表内容
            Object[][] colcontents = new Object[rowsperpage+1][colnames.length];
            //因为表头不显示，所以把表头添加到内容的第一行
            colcontents[0] = new Object[]{"项目","物料描述","数量","发出仓库","VIN码","存放点","条形码"};

            //起始index
            int start = pageIndex*rowsperpage;
            //内容循环赋值
            for(int i=start; i<Math.min(start+rowsperpage, dataList.size()); i++){
                Map<String, Object> data = dataList.get(i);
                colcontents[i-start+1] = new Object[]{
                        i+1,
                        (String)data.get("matName"),
                        StringUtils.isEmpty((String)data.get("carNumber"))?"1":(String)data.get("carNumber"),
                        (String)data.get("productStock"),
                        (String)data.get("vin"),
                        (String)data.get("stockPosition"),
                        invoiceCode};
            }

            //创建表格
            DefaultTableModel model = new DefaultTableModel(colcontents, colnames);
            JTable table = new JTable(model);
            //设置行高：默认16，因为有换行，所以32
            for(int i=1; i<=rowsperpage; i++){
                table.setRowHeight(i, 32);
            }
            //设置字体
            table.setFont(new Font("新宋体", Font.PLAIN, 10));
            //设置栅格(单元格边框)颜色
            table.setGridColor(Color.BLACK);
            
            //设置列宽
            int[] colwidths = {30, 150, 40, 80, 90, 80, 90};
            TableColumn column;
            for(int i=0; i<colnames.length; i++){
                column = table.getColumnModel().getColumn(i);
                column.setWidth(colwidths[i]);
                column.setPreferredWidth(colwidths[i]);
            }

            //表格设置边框
            table.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            //设置表格位置，大小
            table.setBounds(0, 0, table.getPreferredSize().width, table.getPreferredSize().height);
            //设置默认格式：自动换行
//            table.setDefaultRenderer(Object.class, new TableCellTextAreaRenderer());
            //***移动原点***
            g2.translate((width-table.getWidth())/2, tableY);
            table.print(g2);

            BufferedImage image = createImage(table);
            File f=new File("d:\\img1.jpg");
            try {
                if( !f.exists() )
                {
                    f.createNewFile();
                }
                ImageIO.write(image, "jpg", f);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //-------------底线-------------
            float[] dash1 = {2.0f};
            //打印线的属性：1.线宽 2、3、不知道，4、空白的宽度，5、虚线的宽度，6、偏移量
            g2.setStroke(new BasicStroke(0.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 2.0f, dash1, 0.0f));
            //底线Y坐标
            int lineY = table.getHeight()+linedis;
            g2.drawLine(0, lineY, table.getWidth(), lineY);

            //-------------底线下的字-------------
            //底线下的字坐标
            int linetextY = lineY+linetextdis;
            g2.drawString("制单人:张三", 0, linetextY);
            g2.drawString("打印人:李四", 100, linetextY);
            g2.drawString("提车人:王五", 200, linetextY);
            g2.drawString("第"+(pageIndex+1)+"页/共"+pagecnt+"页", 500, linetextY);

            return PAGE_EXISTS;
        }else{
            return NO_SUCH_PAGE;
        }
    }

    public static BufferedImage createImage(JTable table) {
        int totalWidth = table.getSize().width;
        int totalHeight = table.getSize().height;
        BufferedImage tableImage = new BufferedImage(totalWidth, totalHeight, BufferedImage.TYPE_INT_RGB);

        Graphics2D g2D = (Graphics2D) tableImage.createGraphics();

        g2D.setColor(Color.WHITE);
        g2D.fillRect(0, 0, totalWidth, totalHeight);

        g2D.translate(0, 0);
        table.getTableHeader().paint(g2D);

        g2D.translate(0, 0);
        table.paint(g2D);

        g2D.dispose();

        return tableImage;
    }
}

class TableCellTextAreaRenderer extends JTextArea implements TableCellRenderer {
    public TableCellTextAreaRenderer() {
        setLineWrap(true);
        setWrapStyleWord(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        // 计算当下行的最佳高度
        int maxPreferredHeight = 0;
        for (int i = 0; i < table.getColumnCount(); i++) {
            setText("" + table.getValueAt(row, i));
            setSize(table.getColumnModel().getColumn(column).getWidth(), 0);
            maxPreferredHeight = Math.max(maxPreferredHeight, getPreferredSize().height);
        }

        if (table.getRowHeight(row) != maxPreferredHeight) {
            table.setRowHeight(row, maxPreferredHeight);
        }

        setText((value==null) ? "" : value.toString());
        return this;
    }
}