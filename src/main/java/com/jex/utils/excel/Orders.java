package com.jex.utils.excel;

import java.util.Date;

/**
 * Excel测试实体类
 */
public class Orders {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 导入编号
     */
    private String importNo;

    /**
     * 任务编号
     */
    private String taskNo;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 店铺名
     */
    private String shop;

    /**
     * 淘宝账号
     */
    private String taobao;

    /**
     * 录入价格。单位:分
     */
    private Integer enPrice;

    /**
     * 实际价格。单位:分
     */
    private Integer realPrice;

    /**
     * 订单状态：1、代拍下，2、已拍下，3、已付款，4、其他
     */
    private int status;

    /**
     * 截止时间
     */
    private Date endTime;

    /**
     * 标旗状态：1、等待标记，2、标记成功，3、标记失败，4、未订购应用
     */
    private int signStatus;

    /**
     * 获取主键
     *
     * @return id - 主键
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取导入编号
     *
     * @return import_no - 导入编号
     */
    public String getImportNo() {
        return importNo;
    }

    /**
     * 设置导入编号
     *
     * @param importNo 导入编号
     */
    public void setImportNo(String importNo) {
        this.importNo = importNo;
    }

    /**
     * 获取任务编号
     *
     * @return task_no - 任务编号
     */
    public String getTaskNo() {
        return taskNo;
    }

    /**
     * 设置任务编号
     *
     * @param taskNo 任务编号
     */
    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo == null ? null : taskNo.trim();
    }

    /**
     * 获取订单编号
     *
     * @return order_no - 订单编号
     */
    public String getOrderNo() {
        return orderNo;
    }

    /**
     * 设置订单编号
     *
     * @param orderNo 订单编号
     */
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    /**
     * 获取店铺名
     *
     * @return shop - 店铺名
     */
    public String getShop() {
        return shop;
    }

    /**
     * 设置店铺名
     *
     * @param shop 店铺名
     */
    public void setShop(String shop) {
        this.shop = shop == null ? null : shop.trim();
    }

    /**
     * 获取淘宝账号
     *
     * @return taobao - 淘宝账号
     */
    public String getTaobao() {
        return taobao;
    }

    /**
     * 设置淘宝账号
     *
     * @param taobao 淘宝账号
     */
    public void setTaobao(String taobao) {
        this.taobao = taobao == null ? null : taobao.trim();
    }

    /**
     * 获取录入价格。单位:分
     *
     * @return en_price - 录入价格。单位:分
     */
    public Integer getEnPrice() {
        return enPrice;
    }

    /**
     * 设置录入价格。单位:分
     *
     * @param enPrice 录入价格。单位:分
     */
    public void setEnPrice(Integer enPrice) {
        this.enPrice = enPrice;
    }

    /**
     * 获取实际价格。单位:分
     *
     * @return real_price - 实际价格。单位:分
     */
    public Integer getRealPrice() {
        return realPrice;
    }

    /**
     * 设置实际价格。单位:分
     *
     * @param realPrice 实际价格。单位:分
     */
    public void setRealPrice(Integer realPrice) {
        this.realPrice = realPrice;
    }

    /**
     * 获取订单状态：1、代拍下，2、已拍下，3、已付款，4、其他
     *
     * @return status - 订单状态：1、代拍下，2、已拍下，3、已付款，4、其他
     */
    public int getStatus() {
        return status;
    }

    /**
     * 设置订单状态：1、代拍下，2、已拍下，3、已付款，4、其他
     *
     * @param status 订单状态：1、代拍下，2、已拍下，3、已付款，4、其他
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * 获取截止时间
     *
     * @return end_time - 截止时间
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * 设置截止时间
     *
     * @param endTime 截止时间
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * 获取标旗状态：1、等待标记，2、标记成功，3、标记失败，4、未订购应用
     *
     * @return sign_status - 标状态：1、等待标记，2、标记成功，3、标记失败，4、未订购应用
     */
    public int getSignStatus() {
        return signStatus;
    }

    /**
     * 设置标旗状态：1、等待标记，2、标记成功，3、标记失败，4、未订购应用
     *
     * @param signStatus 标状态：1、等待标记，2、标记成功，3、标记失败，4、未订购应用
     */
    public void setSignStatus(int signStatus) {
        this.signStatus = signStatus;
    }



}
