package com.icetech.taskcenter.service.job;


import com.icetech.taskcenter.service.IceSendinfoService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


/**
 * 任务Handler
 * @author wangzw
 */
@JobHandler(value="sendInfoJob")
@Component
public class SendInfoJobHandler extends IJobHandler {

	private final static Integer MAX_NUM = 10;
	@Autowired
	private IceSendinfoService iceSendinfoService;
//
//	@Autowired
//	private SendService sendService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public ReturnT<String> execute(String param) throws Exception {
//		Integer maxNum = null;
//		try {
//			maxNum = Integer.valueOf(param);
//			maxNum = Objects.isNull(maxNum)?MAX_NUM:maxNum;
//		}catch (Exception e){
//			maxNum = MAX_NUM;
//		}
//		//获取所有的未下发的id
//		List<IceSendinfo> iceSendinfos = iceSendinfoService.selectBySendTypeAndNum(IceSendinfo.SendTypeEnum.NO.getCode(),maxNum);
//		if(CollectionUtils.isEmpty(iceSendinfos)){
//			return ReturnT.FAIL;
//		}
//		for (IceSendinfo iceSendinfo:iceSendinfos){
//			SendRequest sendRequest = new SendRequest();
//			sendRequest.setParkId(iceSendinfo.getParkId());
//			sendRequest.setServiceId(iceSendinfo.getServiceId());
//			sendRequest.setServiceType(iceSendinfo.getServiceType());
//			ObjectResponse sendResponse;
//			try {
//				sendResponse = sendService.send(sendRequest);
//			}catch (Exception e){
//				XxlJobLogger.log("<下发任务失败:接口调用出现异常> 车场号：{},业务类型{},业务号:{}",
//						iceSendinfo.getParkId(),
//						iceSendinfo.getServiceType(),
//						iceSendinfo.getServiceId());
//				continue;
//			}
//			if (sendResponse.getCode().equals(CodeConstants.SUCCESS)){
//				iceSendinfo.setSendType(IceSendinfo.SendTypeEnum.YES.getCode());
//				iceSendinfo.setStatus(IceSendinfo.StatusEnum._SUCCESS.getCode());
//				XxlJobLogger.log("<下发任务成功> 车场号：{},业务类型{},业务号:{}",
//						iceSendinfo.getParkId(),
//						iceSendinfo.getServiceType(),
//						iceSendinfo.getServiceId());
//			}else {
//				iceSendinfo.setStatus(IceSendinfo.StatusEnum._ERROR.getCode());
//				XxlJobLogger.log("<下发任务失败> 车场号：{},业务类型{},业务号:{}",
//						iceSendinfo.getParkId(),
//						iceSendinfo.getServiceType(),
//						iceSendinfo.getServiceId());
//			}
//			iceSendinfo.setSendNum(iceSendinfo.getSendNum()+1);
//		}
//		if (!CollectionUtils.isEmpty(iceSendinfos)){
//			iceSendinfoService.batchUpdate(iceSendinfos);
//		}
		return ReturnT.SUCCESS;
	}
}
