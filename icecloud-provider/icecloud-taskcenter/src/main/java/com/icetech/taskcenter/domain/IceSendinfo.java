package com.icetech.taskcenter.domain;

import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.util.Date;

/**
 *  数据下发表
 *
 *  @author wangzw
 */
@Data
public class IceSendinfo implements Serializable {
    private static final long serialVersionUID = 42L;

    /**
     * 主键自增长id
     */
    private int id;

    /**
     * 车场id
     */
    private Long parkId;
    /**
     * 优先级
     */
    private int level;

    /**
     * 业务类型（1月卡 2云预缴费）
     */
    private int serviceType;

    /**
     * 业务id  如业务类型为1此类为月卡操作记录表的ID；
     */
    private Long serviceId;

    /**
     * 状态(1成功2失败默认0
     */
    private int status;

    /**
     * 是否下发（0未下发，1已下发默认0）
     */
    private int sendType;

    /**
     * 发送次数
     */
    private int sendNum;

    /**
     * 人工下发操作人 针对月卡的操作
     */
    private String updateUser;

    /**
     * 备注 重点记录下发失败的原因
     */
    private String remark;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 操作时间 人工下发操作时间
     */
    private Date updateTime;

    public enum SendTypeEnum{
        YES(1),
        NO(0),
        ;
        private @Getter
        int code;
        SendTypeEnum(int code){
            this.code = code;
        }
    }

    public enum StatusEnum{
        _DEFAULT(0),
        _SUCCESS(1),
        _ERROR(2),
        ;
        private @Getter
        int code;
        StatusEnum(int code){
            this.code = code;
        }
    }
}