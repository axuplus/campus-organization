package com.safe.campus.about.utils;

import org.apache.poi.POIXMLDocumentPart;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class POIExcel {
    public static void getDataFromExcel(String filePath) throws IOException {
        // 判断是否为excel类型文件
        if (!filePath.endsWith(".xls") && !filePath.endsWith(".xlsx")) {
            System.out.println("文件不是excel类型");
        }
        FileInputStream fis = null;
        Workbook wookbook = null;
        Sheet sheet = null;
        try {
            //获取一个绝对地址的流
            fis = new FileInputStream(filePath);
            /* 读取网络文件（比如七牛等云存储）
            URL url = new URL(filePath);
            BufferedInputStream fis = new BufferedInputStream(url.openStream());*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //2003版本的excel，用.xls结尾
            wookbook = new HSSFWorkbook(fis);//得到工作簿
        } catch (Exception ex) {
            //ex.printStackTrace();
            try {
                //2007版本的excel，用.xlsx结尾
                fis = new FileInputStream(filePath);
                wookbook = new XSSFWorkbook(fis);//得到工作簿
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        Map<String, PictureData> maplist = null;
        sheet = wookbook.getSheetAt(0);
        // 判断用07还是03的方法获取图片
        if (filePath.endsWith(".xls")) {
            maplist = getPictures1((HSSFSheet) sheet);
        } else if (filePath.endsWith(".xlsx")) {
            maplist = getPictures2((XSSFSheet) sheet);
        }
        try {
            printImg(maplist);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //释放map
            if (maplist != null) {
                maplist = null;
            }
        }
        //得到一个工作表
        //获得表头
        Row rowHead = sheet.getRow(0);

        //获得数据的总行数
        int totalRowNum = sheet.getLastRowNum();
        //要获得属性
        String name = "";
        String phone = "";
        String space="";
        String pic = "";
        // 获得所有数据
        for (int i = 1; i < totalRowNum; i++) {
            // 获得第i行对象
            Row row = sheet.getRow(i);

            // 空间位置(为空则停止解析)
            Cell cell = row.getCell(0);
            if (cell == null) {
                break;
            }
            cell.setCellType(CellType.STRING);
            space =cell.getStringCellValue().toString();
            if (StringUtils.isBlank(space)){
                break;
            }
            // 名称
            cell = row.getCell(1);
            if (cell != null) {
                cell.setCellType(CellType.STRING);
                name = cell.getStringCellValue();
            }
            // 手机号
            cell = row.getCell(2);
            if (cell != null) {
                cell.setCellType(CellType.STRING);
                phone = cell.getStringCellValue();
            }
            System.out.println(name + "\t\t" + phone);
        }
        for (Map.Entry<String, PictureData> entry : maplist.entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }
        //使用完成关闭
        wookbook.close();
        if (fis != null) {
            fis.close();
        }
    }

    /**
     * 获取图片和位置 (xls)
     *
     * @param sheet
     * @return
     * @throws IOException
     */
    public static Map<String, PictureData> getPictures1(HSSFSheet sheet) throws IOException {
        Map<String, PictureData> map = new HashMap<String, PictureData>();
        List<HSSFShape> list = sheet.getDrawingPatriarch().getChildren();
        for (HSSFShape shape : list) {
            if (shape instanceof HSSFPicture) {
                HSSFPicture picture = (HSSFPicture) shape;
                HSSFClientAnchor cAnchor = (HSSFClientAnchor) picture.getAnchor();
                PictureData pdata = picture.getPictureData();
                String key = cAnchor.getRow1() + "-" + cAnchor.getCol1(); // 行号-列号
                map.put(key, pdata);
            }
        }
        return map;
    }

    /**
     * 获取图片和位置 (xlsx)
     *
     * @param sheet
     * @return
     * @throws IOException
     */
    public static Map<String, PictureData> getPictures2(XSSFSheet sheet) throws IOException {
        Map<String, PictureData> map = new HashMap<String, PictureData>();
        List<POIXMLDocumentPart> list = sheet.getRelations();
        for (POIXMLDocumentPart part : list) {
            if (part instanceof XSSFDrawing) {
                XSSFDrawing drawing = (XSSFDrawing) part;
                List<XSSFShape> shapes = drawing.getShapes();
                for (XSSFShape shape : shapes) {
                    XSSFPicture picture = (XSSFPicture) shape;
                    XSSFClientAnchor anchor = picture.getPreferredSize();
                    CTMarker marker = anchor.getFrom();
                    String key = marker.getRow() + "-" + marker.getCol();
                    map.put(key, picture.getPictureData());
                }
            }
        }
        return map;
    }

    //图片写出
    public static void printImg(Map<String, PictureData> sheetList) throws Exception {
        Object key[] = sheetList.keySet().toArray();
        String filePath = "";
        for (int i = 0; i < sheetList.size(); i++) {
            // 获取图片流
            PictureData pic = sheetList.get(key[i]);
            // 获取图片索引
            String picName = key[i].toString();
            // 获取图片格式
            String ext = pic.suggestFileExtension();
            byte[] data = pic.getData();
            //文件上传七牛
//            QiNiuUtils.uploadOneObject(data,"111_"+picName + "." + ext);
            //图片保存路径
            filePath = "D:\\img\\pic" + picName + "." + ext;
            System.out.println(filePath);
            FileOutputStream out = new FileOutputStream(filePath);
            out.write(data);
            out.close();
        }
    }

    public static void main(String[] args) throws Exception {
        getDataFromExcel("C:\\Users\\14256\\Desktop"+ File.separator +"pacture.xlsx");
    }

}
