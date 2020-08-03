package com.fh.Settle.controller;

import com.fh.Settle.model.Settle;
import com.fh.Settle.service.SettleService;
import com.fh.common.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("settle")
@RestController
public class SettleController {

    @Autowired
    private SettleService settleService;

    @RequestMapping("addSettle")
    public ResponseCode addSettle(Settle settle){
        return settleService.addSettle(settle);
    }

    @RequestMapping("queryList")
    public ResponseCode queryList(){
        return settleService.queryList();
    }

    @RequestMapping("queryStatusList")
    public ResponseCode queryStatusList(){
        return settleService.queryStatusList();
    }

    @RequestMapping("deleteSettle/{id}")
    public ResponseCode deleteSettle(@PathVariable("id") Integer id){
        return settleService.deleteSettle(id);
    }

    @RequestMapping("updateStatus/{id}")
    public ResponseCode updateStatus(@PathVariable("id") Integer id){
        return settleService.updateStatus(id);
    }

    @RequestMapping("updSettle")
    public ResponseCode updSettle(Settle settle){
        return settleService.updSettle(settle);
    }

}
