package com.icetech.taskcenter.service;

import com.icetech.taskcenter.domain.IceSendinfo;
import com.xxl.job.core.biz.model.ReturnT;

import java.util.List;

/**
 * 数据下发表
 *
 * @author wangzw
 */
public interface IceSendinfoService {

    /**
     * 新增
     */
    public ReturnT<String> insert(IceSendinfo iceSendinfo);

    /**
     * 删除
     */
    public ReturnT<String> delete(int id);

    /**
     * 更新
     */
    public ReturnT<String> update(IceSendinfo iceSendinfo);

    /**
     * Load查询
     */
    public IceSendinfo load(int id);

    /**
     * 查询需要发送通知的订单信息
     */
    public List<IceSendinfo> selectBySendTypeAndNum(int type, int maxNum);

    /**
     * 批量更新
     * @param collect
     * @return
     */
    int batchUpdate(List<IceSendinfo> collect);
}
