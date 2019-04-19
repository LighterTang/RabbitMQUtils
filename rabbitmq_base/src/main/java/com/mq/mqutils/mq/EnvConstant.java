
package com.mq.mqutils.mq;


public class EnvConstant {

    //消息队列
    public static String NODE_PASS_QUEUE_NAME = "node-pass-test";
    public static String PASS_DETEC_QUEUE_NAME = "pass-detec-test";
    public static String PASSDETEC_WEB_QUEUE_NAME   = "passdetec-web-test";
    public static String SYSTEM_WEB_QUEUE_NAME   = "system-web-test";
    public static String HOST   = "";


    public static Integer MESSAGE_QUEUE_PORT = 5672;
    public static String MESSAGE_QUEUE_USERNAME = "";
    public static String MESSAGE_QUEUE_PASSWORD = "";
    public static String TIME_LIMIT = "";
    /**
     * 超级管理员
     */
    public static String SUPERADMIN = "";


    /**
     * 从配置文件读取参数
     */
    static {
        PropertiesUtil propertiesUtil = new PropertiesUtil("/base.properties");
        HOST = propertiesUtil.getPropertieValue("MESSAGE_QUEUE_HOST");
        MESSAGE_QUEUE_USERNAME = propertiesUtil.getPropertieValue("MESSAGE_QUEUE_USERNAME");
        MESSAGE_QUEUE_PASSWORD = propertiesUtil.getPropertieValue("MESSAGE_QUEUE_PASSWORD");
        TIME_LIMIT = propertiesUtil.getPropertieValue("TIME_LIMIT");
        MESSAGE_QUEUE_PORT = Integer.valueOf(propertiesUtil.getPropertieValue("MESSAGE_QUEUE_PORT"));
    }

}
