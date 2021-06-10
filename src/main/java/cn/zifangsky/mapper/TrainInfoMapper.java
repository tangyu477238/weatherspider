package cn.zifangsky.mapper;

import cn.zifangsky.model.TrainInfo;

import java.util.List;

public interface TrainInfoMapper {


    int insert(TrainInfo train);

    TrainInfo getTrainInfo(TrainInfo train);

//    int update(Train train);

    List<TrainInfo> listTrainInfo(TrainInfo train);

}