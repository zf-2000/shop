package com.fh.util;

public class SystemConstant {

    public final static String SESSION_KEY = "user";
    public final static int COOKIE_EXPIRY_TIME=7*24*60*60;
    public final static int REDIS_EXPIRY_TIME=5*60;

    public final static String LOGIN_COOLOE_KEY= "login_cookie";
    public final static String COOKIE_USER_ID="cook_user_id";
    public final static String CATR_KEY="cart:";
    public final static String Token_KEY="token";
    public final static String IS_NOT_AMOUNT="无货";
    public final static String IN_STORE_AMOUNT="有货";
    public final static int ORDER_STATUS_WAIT=1;//未完成
    public final static int ORDER_STATUS_FINISH=2;//已完成
    public final static String REDIS_CATEGORY_KEY="category:";//分类列表展示数据
    public final static String REDIS_PRPDUCT_ISHOT="hot-sell:";//是否热销产品
    public final static String REDIS_PRPDUCT="product:";
}
