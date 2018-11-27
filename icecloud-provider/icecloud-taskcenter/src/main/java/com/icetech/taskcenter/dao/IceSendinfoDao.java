package com.icetech.taskcenter.dao;

import com.icetech.taskcenter.domain.IceSendinfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 数据下发表
 *
 * @author wangzw
 */
public interface IceSendinfoDao {

    /**
     * 新增
     */
    public int insert(@Param("iceSendinfo") IceSendinfo iceSendinfo);

    /**
     * 删除
     */
    public int delete(@Param("id") int id);

    /**
     * 更新
     */
    public int update(@Param("iceSendinfo") IceSendinfo iceSendinfo);

    /**
     * Load查询
     */
    public IceSendinfo load(@Param("id") int id);

    /**
     * 根据类型获取发送信息
     * @param sendType
     * @return
     */
    List<IceSendinfo> selectBySendTypeAndNum(@Param("sendType") int sendType, @Param("maxNum") int maxNum);

    /**
     * 批量更新
     * @param record
     * @return
     */
    int batchUpdate(@Param("record") List<IceSendinfo> record);

    /**
     * 根据业务id 获取下发记录
     * @param serviceType
     * @param serviceId
     * @return
     */
    IceSendinfo selectByServiceId(@Param("serviceType") Integer serviceType, @Param("serviceId") Integer serviceId);
}
