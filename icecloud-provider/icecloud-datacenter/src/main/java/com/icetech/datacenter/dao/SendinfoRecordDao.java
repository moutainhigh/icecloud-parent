package com.icetech.datacenter.dao;

import com.icetech.common.dao.BaseDao;
import com.icetech.datacenter.domain.SendinfoRecord;
import org.apache.ibatis.annotations.Param;

public interface SendinfoRecordDao extends BaseDao<SendinfoRecord> {

    /**
     * 根据消息Id查询单条记录
     * @param messageId
     * @param operType
     * @return
     */
    SendinfoRecord selectOneByMsgId(@Param("messageId") String messageId,
                                    @Param("operType") int operType);

}