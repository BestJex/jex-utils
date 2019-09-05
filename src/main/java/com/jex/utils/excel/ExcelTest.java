package com.jex.utils.excel;


import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;

/**
 * Excel测试
 */
public class ExcelTest {

    public static List<Orders> importOrderExcel() throws Exception{

        String importNo = null;
        importNo = String.format("%s",System.currentTimeMillis());
        File file = new File("D:\\Java project\\jex-utils\\src\\main\\resources\\excel_import_test.xlsx");
         FileInputStream in= new FileInputStream(file);

        List<List<Object>> listob = ExcelUtils.getBankListByExcel(in,file.getName());
        List<Orders> ordersList = new ArrayList<Orders>();
        //遍历listob数据，把数据放到List中
        for (int i = 0; i < listob.size() ; i++) {
            List<Object> ob = listob.get(i);
            Orders orders = new Orders();
            orders.setImportNo(importNo);
            //通过遍历实现把每一列封装成一个model中，再把所有的model用List集合装载
            orders.setTaskNo(String.valueOf(ob.get(0)));
            orders.setOrderNo(String.valueOf(ob.get(1)));
            orders.setShop(String.valueOf(ob.get(2)));
            orders.setTaobao(String.valueOf(ob.get(3)));
            orders.setEnPrice(Integer.valueOf(ob.get(4).toString())*100);
            orders.setRealPrice(Integer.valueOf(ob.get(5).toString())*100);
            orders.setStatus(Integer.valueOf(ob.get(6).toString()));
            orders.setSignStatus(Integer.valueOf(ob.get(7).toString()));
            orders.setEndTime(DateUtils.parseDate(ob.get(8).toString(),"yyyy-MM-dd"));
            ordersList.add(orders);
        }
        System.out.println(ordersList.size());
        return  ordersList;
    }

    public static XSSFWorkbook exportOrderExcel() throws Exception {

        List<Orders> orderList = ExcelTest.importOrderExcel();

        // 格式化查询出的数据
        List<Orders> orders = new ArrayList<Orders>();
        for (Orders order : orderList) {
            Orders item = new Orders();
            item.setId(order.getId());
            item.setImportNo(order.getImportNo());
            item.setTaskNo(order.getTaskNo());
            item.setOrderNo(order.getOrderNo());
            item.setShop(order.getShop());
            item.setTaobao(order.getTaobao());
            item.setEnPrice(order.getEnPrice() / 100);
            item.setRealPrice(order.getRealPrice() / 100);
            item.setStatus(order.getStatus());
            item.setSignStatus(order.getSignStatus());
            item.setEndTime(order.getEndTime());
            orders.add(item);
        }

        List<ExcelBean> excel = new ArrayList<ExcelBean>();
        Map<Integer, List<ExcelBean>> map = new LinkedHashMap<Integer, List<ExcelBean>>();
        XSSFWorkbook xssfWorkbook = null;
        //设置标题栏
        excel.add(new ExcelBean("序号", "id", 0));
        excel.add(new ExcelBean("导入编号", "importNo", 0));
        excel.add(new ExcelBean("任务编号", "taskNo", 0));
        excel.add(new ExcelBean("订单编号", "orderNo", 0));
        excel.add(new ExcelBean("店铺名", "shop", 0));
        excel.add(new ExcelBean("录入价格", "enPrice", 0));
        excel.add(new ExcelBean("实际价格", "realPrice", 0));
        excel.add(new ExcelBean("订单状态：1、代拍下，2、已拍下，3、已付款，4、其他", "status", 0));
        excel.add(new ExcelBean("标旗状态：1、等待标记，2、标记成功，3、标记失败，4、未订购应用", "signStatus", 0));
        excel.add(new ExcelBean("截止时间", "endTime", 0));
        map.put(0, excel);
        String sheetName = "测试";
        //调用ExcelUtil的方法
        xssfWorkbook = ExcelUtils.createExcelFile(Orders.class, orders, map, sheetName);

        File outFile = new File("D:\\Java project\\jex-utils\\src\\main\\resources\\excel_export_test.xlsx");
        FileOutputStream out = new FileOutputStream(outFile);
        BufferedOutputStream bufferedOutPut = new BufferedOutputStream(out);
        bufferedOutPut.flush();
        xssfWorkbook.write(bufferedOutPut);
        bufferedOutPut.close();
        return xssfWorkbook;
    }




    public static void main(String[] args) throws Exception {

        ExcelTest.importOrderExcel();
//        ExcelTest.exportOrderExcel();
    }
}
