package com.icetech.datacenter.dao;

import com.icetech.datacenter.domain.MonthProduct;

public interface MonthProductDao {

    MonthProduct selectById(Long id);

}