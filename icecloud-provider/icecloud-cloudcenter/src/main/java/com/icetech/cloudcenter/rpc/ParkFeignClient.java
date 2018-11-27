package com.icetech.cloudcenter.rpc;

import com.icetech.api.cloudcenter.service.ParkFeignApi;
import com.icetech.cloudcenter.dao.park.ParkDao;
import com.icetech.common.constants.CodeConstants;
import com.icetech.common.domain.Park;
import com.icetech.common.domain.response.ObjectResponse;
import com.icetech.common.util.ResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("parkService")
public class ParkFeignClient implements ParkFeignApi {
    // 日志管理
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ParkDao parkDao;

    @Override
    public ObjectResponse<Park> findByParkId(Long parkId) {
        Park park = parkDao.selectById(parkId);
        if (park != null){
            return ResponseUtils.returnSuccessResponse(park);
        }else{
            logger.info("<车场信息查询> 根据parkId未找到车场信息，parkId：{}",parkId);
            return ResponseUtils.returnErrorResponse(CodeConstants.ERROR_404);
        }
    }

    @Override
    public ObjectResponse<Park> findByParkCode(String parkCode) {
        Park park = parkDao.selectByCode(parkCode);
        if (park != null){
            return ResponseUtils.returnSuccessResponse(park);
        }else{
            logger.info("<车场信息查询> 根据parkCode未找到车场信息，parkCode：{}",parkCode);
            return ResponseUtils.returnErrorResponse(CodeConstants.ERROR_404);
        }
    }
}
