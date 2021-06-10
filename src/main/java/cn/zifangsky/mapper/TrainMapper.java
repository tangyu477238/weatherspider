package cn.zifangsky.mapper;

import cn.zifangsky.model.Train;

public interface TrainMapper {


    int insert(Train train);

    Train getTrain(Train train);

    int update(Train train);

}