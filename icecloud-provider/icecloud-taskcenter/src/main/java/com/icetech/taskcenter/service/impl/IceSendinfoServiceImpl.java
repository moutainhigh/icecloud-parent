package com.icetech.taskcenter.service.impl;

import com.icetech.taskcenter.dao.IceSendinfoDao;
import com.icetech.taskcenter.domain.IceSendinfo;
import com.icetech.taskcenter.service.IceSendinfoService;
import com.xxl.job.core.biz.model.ReturnT;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 数据下发表
 *
 * @author wangzw
 */
@Service
public class IceSendinfoServiceImpl implements IceSendinfoService {

    @Resource
    private IceSendinfoDao iceSendinfoDao;

    /**
     * 新增
     */
    @Override
    public ReturnT<String> insert(IceSendinfo iceSendinfo) {

        // valid
        if (iceSendinfo == null) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "必要参数缺失");
        }

        iceSendinfoDao.insert(iceSendinfo);
        return ReturnT.SUCCESS;
    }

    /**
     * 删除
     */
    @Override
    public ReturnT<String> delete(int id) {
        int ret = iceSendinfoDao.delete(id);
        return ret>0?ReturnT.SUCCESS:ReturnT.FAIL;
    }

    /**
     * 更新
     */
    @Override
    public ReturnT<String> update(IceSendinfo iceSendinfo) {
        int ret = iceSendinfoDao.update(iceSendinfo);
        return ret>0?ReturnT.SUCCESS:ReturnT.FAIL;
    }

    /**
     * Load查询
     */
    @Override
    public IceSendinfo load(int id) {
        return iceSendinfoDao.load(id);
    }

    /**
     * 根据下发类型获取下发信息
     * @param type
     * @return
     */
    @Override
    public List<IceSendinfo> selectBySendTypeAndNum(int type, int maxNum) {
        return iceSendinfoDao.selectBySendTypeAndNum(type,maxNum);
    }

    @Override
    public int batchUpdate(List<IceSendinfo> collect) {
        return iceSendinfoDao.batchUpdate(collect);
    }
}
