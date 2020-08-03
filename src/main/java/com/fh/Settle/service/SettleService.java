package com.fh.Settle.service;

import com.fh.Settle.model.Settle;
import com.fh.common.ResponseCode;

public interface SettleService {
    ResponseCode addSettle(Settle settle);

    ResponseCode queryList();

    ResponseCode deleteSettle(Integer id);

    ResponseCode queryStatusList();

    ResponseCode updateStatus(Integer id);

    ResponseCode updSettle(Settle settle);

}
