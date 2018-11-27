package com.icetech.datacenter.dao;


import com.icetech.datacenter.domain.ParkFreespace;

/**
 * Description : 车场车位表DAO
 * @author fangct
 */
public interface ParkFreespaceDao {

    int updateFreeSpace(ParkFreespace parkFreespace);
}
