package com.icetech.taskcenter.rpc;

import com.icetech.api.taskcenter.service.SendInfoFeignApi;
import com.icetech.common.constants.CodeConstants;
import com.icetech.common.domain.response.ObjectResponse;
import com.icetech.taskcenter.dao.IceSendinfoDao;
import com.icetech.taskcenter.domain.IceSendinfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * dubbo 接口 暴露 实现类
 * @author wangzw
 */
@RestController
public class SendInfoFeignClient implements SendInfoFeignApi {

    @Autowired
    private IceSendinfoDao iceSendinfoDao;

    @Override
    public ObjectResponse notifySuccess(Integer serviceType, Integer serviceId) {
        IceSendinfo iceSendinfo = iceSendinfoDao.selectByServiceId(serviceType,serviceId);
        if (Objects.isNull(iceSendinfo)){
            ObjectResponse objectResponse = new ObjectResponse(CodeConstants.ERROR_404,CodeConstants.getName(CodeConstants.ERROR_404));
            return objectResponse;

        }
        //更新为成功状态
        iceSendinfo.setStatus(IceSendinfo.StatusEnum._SUCCESS.getCode());
        iceSendinfo.setSendType(IceSendinfo.SendTypeEnum.YES.getCode());
        iceSendinfoDao.update(iceSendinfo);
        ObjectResponse objectResponse = new ObjectResponse(CodeConstants.SUCCESS,CodeConstants.getName(CodeConstants.SUCCESS));
        return objectResponse;
    }
}
