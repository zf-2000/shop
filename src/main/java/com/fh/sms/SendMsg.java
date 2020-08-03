package com.fh.sms;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.fh.common.ResponseCode;
import com.fh.member.util.MessageVerifyUtils;
import com.fh.util.RedisUtil;
import com.fh.util.SystemConstant;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("sms")
@RestController
public class SendMsg {

    @RequestMapping("sendmsg")
    public ResponseCode sendMsg(String phone){
        String newcode = MessageVerifyUtils.getNewcode();
        try {
            SendSmsResponse sendSmsResponse = MessageVerifyUtils.sendSms(phone, newcode);
            if(sendSmsResponse != null && sendSmsResponse.getCode().equals("OK")){
                //把code 放到Redis中
                RedisUtil.set(phone,newcode);
                return ResponseCode.success();
            }
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return ResponseCode.success();
    }
}
