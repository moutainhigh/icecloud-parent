package com.icetech.datacenter.dao;


import com.icetech.datacenter.domain.Heartbeat;

/**
 * Description : 心跳包DAO
 * @author fangct
 */
public interface HeartbeatDao {

    void insertStatusInfo(Heartbeat heartbeat);

    void updateFreeSpace(Heartbeat heartbeat);

    Heartbeat getLast(Long parkId);

    void updateStatus(Heartbeat heartbeat);

}
