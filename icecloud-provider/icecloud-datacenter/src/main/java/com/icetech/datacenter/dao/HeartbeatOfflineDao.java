package com.icetech.datacenter.dao;

import com.icetech.datacenter.domain.HeartbeatOffline;

/**
 * Description : 心跳包掉线DAO
 * @author fangct
 */
public interface HeartbeatOfflineDao {

    void insertOffLine(HeartbeatOffline heartbeatOffline);

}
