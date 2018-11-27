package com.icetech.transcenter.mapper;


import com.icetech.common.dao.BaseDao;
import com.icetech.transcenter.domain.ThirdInfo;
import org.apache.ibatis.annotations.Param;

/**
 * 第三方信息DAO
 * @author fangct
 */
public interface ThirdInfoDao extends BaseDao<ThirdInfo> {

    ThirdInfo selectByParkCode(@Param("parkCode") String parkCode);
}
