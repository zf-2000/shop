package com.fh.pay;

import com.fh.common.ResponseCode;
import com.fh.conf.MyConfig;
import com.fh.conf.WXPay;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("pay")
public class payController {

    @RequestMapping("createNative")
    public ResponseCode createNative(String orderNo, BigDecimal totalPrice){
        try {
            MyConfig config = new MyConfig();
            WXPay wxpay = new WXPay(config);
            Map<String, String> data = new HashMap<String, String>();
            data.put("body", "飞哥哥在线支付");
            data.put("out_trade_no", orderNo);
            data.put("device_info", "WEB");
            data.put("fee_type", "CNY");
            data.put("total_fee", "1");
            data.put("spbill_create_ip", "123.12.12.123");
            data.put("notify_url", "http://www.example.com/wxpay/notify");
            data.put("trade_type", "NATIVE");  // 此处指定为扫码支付
            data.put("product_id", "12");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String format = sdf.format(DateUtils.addMinutes(new Date(), 2));
            data.put("time_expire", format);
            Map<String, String> resp = wxpay.unifiedOrder(data);
            System.out.println(resp);
            //判断通信是否成功
            if(!resp.get("return_code").equalsIgnoreCase("SUCCESS")){
                return  ResponseCode.error("微信平台报错："+resp.get("return_msg"));
            }

            //判断业务
            if(!resp.get("result_code").equalsIgnoreCase("SUCCESS")){
                return  ResponseCode.error("微信平台报错"+resp.get("err_code_des"));
            }

            String code_url = resp.get("code_url");

            return ResponseCode.success(code_url);

        } catch (Exception e) {
            e.printStackTrace();
              return ResponseCode.error(e.getMessage());
        }

    }

    @RequestMapping("queryOrderStatus")
    public ResponseCode queryOrderStatus(String orderNo){
        try {
        MyConfig config = new MyConfig();
        WXPay wxpay = new WXPay(config);

        Map<String, String> data = new HashMap<String, String>();
        data.put("out_trade_no", orderNo);
        int count = 0;
        for (;;){
            Map<String, String> resp = wxpay.orderQuery(data);
            System.out.println(resp);
            //判断通信是否成功
            if(!resp.get("return_code").equalsIgnoreCase("SUCCESS")){
                return ResponseCode.error("微信平台报错："+resp.get("return_msg"));
            }

            //判断业务
            if(!resp.get("result_code").equalsIgnoreCase("SUCCESS")){
                return ResponseCode.error("微信平台报错"+resp.get("err_code_des"));
            }

            if(resp.get("trade_state").equalsIgnoreCase("SUCCESS")){
                return ResponseCode.success();
            }
            count++;
            Thread.sleep(2000);
            if(count>30){
                return ResponseCode.error("支付超时");
            }
        }
        //交易状态
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseCode.error(e.getMessage());
        }

    }
}
