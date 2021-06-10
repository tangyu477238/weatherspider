package cn.zifangsky.spider.train;

import cn.zifangsky.mapper.TrainInfoMapper;
import cn.zifangsky.model.TrainInfo;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 自定义Pipeline处理抓取的数据
 * @author zifangsky
 *
 */
@Component("trainInfoPipeline")
public class TrainInfoPipeline implements Pipeline {
	@Resource
	private TrainInfoMapper trainInfoMapper;

	
	/**
	 * 保存数据
	 */
	@Override
	public void process(ResultItems resultItems, Task task) {

		String str = resultItems.get("trainInfo");
		List<String> trainInfoListString = JSONObject.parseArray(
				JSONObject.parseObject(str)
						.getJSONObject("data")
						.getString("data"),String.class);

		String trainNum = "";
		List<TrainInfo> trainInfoList = new ArrayList<>();
		for (String trainInfoString : trainInfoListString){
			JSONObject map = JSONObject.parseObject(trainInfoString);
			TrainInfo trainInfo = new TrainInfo();
			if (map.containsKey("station_train_code")){
				trainNum = map.getString("station_train_code");
			}
			trainInfo.setTrainNum(trainNum);
			trainInfo.setArriveTime(map.getString("arrive_time"));
			trainInfo.setStartTime(map.getString("start_time"));
			trainInfo.setStationName(map.getString("station_name"));
			trainInfo.setSort(map.getString("station_no"));
			trainInfoList.add(trainInfo);
		}
		for (TrainInfo trainInfo : trainInfoList){
			TrainInfo train1 = trainInfoMapper.getTrainInfo(trainInfo);
			if (train1==null){
				trainInfoMapper.insert(trainInfo);
			}
		}

	}


}
