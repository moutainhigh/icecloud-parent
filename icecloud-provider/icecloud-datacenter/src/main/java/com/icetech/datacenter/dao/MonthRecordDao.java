package com.icetech.datacenter.dao;

import com.icetech.datacenter.domain.MonthRecord;

public interface MonthRecordDao {

    MonthRecord selectById(Long id);

}