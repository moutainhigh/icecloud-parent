package com.icetech.cloudcenter.dao.park;

import com.icetech.cloudcenter.domain.park.ParkConfig;
import org.apache.ibatis.annotations.Param;

/**
 * 停车场系统配置表
 *
 * Created by wangzw on '2018-10-29 09:55:14'.
 */
public interface ParkConfigDao{

    /**
     * Load查询
     */
    ParkConfig selectById(@Param("id") int id);

    /**
     * 车场id查询
     */
    ParkConfig selectByParkId(@Param("parkId") Long parkId);

}
