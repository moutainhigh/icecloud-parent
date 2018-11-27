package com.icetech.paycenter.mapper;

import com.icetech.common.dao.BaseDao;
import com.icetech.paycenter.domain.ThirdInfo;
import org.apache.ibatis.annotations.Param;

/**
 * 第三方信息DAO
 * @author fangct
 */
public interface ThirdInfoDao extends BaseDao<ThirdInfo> {
    ThirdInfo selectByParkCode(@Param("parkCode") String parkCode);
}
