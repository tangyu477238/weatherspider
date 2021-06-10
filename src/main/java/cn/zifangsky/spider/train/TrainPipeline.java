package cn.zifangsky.spider.train;

import cn.zifangsky.manager.CrawlManager;
import cn.zifangsky.mapper.TrainMapper;
import cn.zifangsky.model.TicketInfoDTO;
import cn.zifangsky.model.Train;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 自定义Pipeline处理抓取的数据
 * @author zifangsky
 *
 */
@Component("trainPipeline")
public class TrainPipeline implements Pipeline {
	@Resource
	private TrainMapper trainMapper;

	@Resource
	private CrawlManager crawlManager;

	
	/**
	 * 保存数据
	 */
	@Override
	public void process(ResultItems resultItems, Task task) {

		String str = resultItems.get("trainInfo");
		Train train ;

		List<String> trainInfoListString = JSONObject.parseArray(
				JSONObject.parseObject(str)
						.getJSONObject("data")
						.getString("result"),String.class);

		JSONObject map = JSONObject.parseObject(str).getJSONObject("data").getJSONObject("map");

		List<TicketInfoDTO> trainInfoList = new ArrayList<>();
		for (String trainInfoString: trainInfoListString){
			String[] trainInfoArray =  trainInfoString.split("\\|");
			// not have eligible train time
//			if (!trainTimeEligible(trainInfoArray[8], afterTime, beforeTime) || "".equals(trainInfoArray[0])){
//				continue;
//			}
//			// if the user has set train name and eligible
//			if (trainName != null && trainInfoArray[3].equals(trainName.trim())){
//				List<TrainInfo>  eligibleTrainNameList = new ArrayList<>();
//				eligibleTrainNameList.add(getTrainInfo(trainInfoArray));
//				return eligibleTrainNameList;
//			}
			trainInfoList.add(getTrainInfo(trainInfoArray, map));
		}

		for (TicketInfoDTO infoDTO : trainInfoList){
			train = new Train();
			train.setTrainNum(infoDTO.getTrainNum());
			train.setTrainNo(infoDTO.getTrainNo());
			train.setFromDate(infoDTO.getBizDate());
			train.setFromName(infoDTO.getFormStationName());
			train.setToDate(infoDTO.getBizDate());
			train.setToName(infoDTO.getToStationName());

			train.setFromTime(infoDTO.getGoOffTime());
			train.setToTime(infoDTO.getArrivalTime());
			train.setNum(getSeatNum(infoDTO.getL2Seat()));

			Train train1 = trainMapper.getTrain(train);
			if (train1==null){
				trainMapper.insert(train);
			} else {
				trainMapper.update(train);
			}
			//查询所有数据
			crawlManager.trainInfoCrawl(train.getTrainNo(), infoDTO.getFormStationTelecode(),
					infoDTO.getToStationTelecode(), dateToDate(infoDTO.getBizDate()));
		}


	
	}


	public static String dateToDate(String strDate) {
		Date date  = null; //生成时间对象
		SimpleDateFormat format = null;
		try {
			// 从字符串提取出日期
			String pat = "yyyyMMdd";
			String pat2 = "yyyy-MM-dd";
			SimpleDateFormat format2 = new SimpleDateFormat(pat);
			date = format2.parse(strDate);
			// 将时间对象格式化为字符串
			format = new SimpleDateFormat(pat2);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return format.format(date);
	}

	private BigDecimal getSeatNum(String seatNum){
		if (seatNum.equals("无")||"".equals(seatNum)){
			return new BigDecimal(0);
		} else if (seatNum.equals("有")){
			return new BigDecimal(99);
		} else {
			return new BigDecimal(seatNum);
		}
	}
	private TicketInfoDTO getTrainInfo(String[] trainInfoArray, JSONObject map){

		TicketInfoDTO ticketInfoDTO = new TicketInfoDTO();
		ticketInfoDTO.setSecretStr(trainInfoArray[0]);
		ticketInfoDTO.setOnSale("预订".equals(trainInfoArray[1]));
		ticketInfoDTO.setTrainNo(trainInfoArray[2]);
		ticketInfoDTO.setTrainNum(trainInfoArray[3]);
		ticketInfoDTO.setFormStationTelecode(trainInfoArray[6]);
		ticketInfoDTO.setToStationTelecode(trainInfoArray[7]);
		ticketInfoDTO.setGoOffTime(trainInfoArray[8]);
		ticketInfoDTO.setArrivalTime(trainInfoArray[9]);
		ticketInfoDTO.setLastTime(trainInfoArray[10]);
		ticketInfoDTO.setLeftTicket(trainInfoArray[12]);
		ticketInfoDTO.setTrainLocation(trainInfoArray[15]);

		ticketInfoDTO.setBizDate(trainInfoArray[13]);

		ticketInfoDTO.setBusinessSeat(trainInfoArray[32]);   // or 5
		ticketInfoDTO.setL1Seat(trainInfoArray[31]);
		ticketInfoDTO.setL2Seat(trainInfoArray[30]);
		ticketInfoDTO.setL1SoftBerth(trainInfoArray[23]);
		ticketInfoDTO.setL2HardBerth(trainInfoArray[28]);
		ticketInfoDTO.setSoftSeat(trainInfoArray[24]);
		ticketInfoDTO.setHardSeat(trainInfoArray[29]);
		ticketInfoDTO.setNoSeat(trainInfoArray[26]);
		ticketInfoDTO.setCanAlternate(trainInfoArray[37].equals("1"));
		if (trainInfoArray.length == 38) {
			ticketInfoDTO.setCanNotAlternateSeatType("");
		}
		if (trainInfoArray.length == 39) {
			ticketInfoDTO.setCanNotAlternateSeatType(trainInfoArray[38]);
		}

		ticketInfoDTO.setFormStationName(map.get(ticketInfoDTO.getFormStationTelecode()).toString());
		ticketInfoDTO.setToStationName(map.get(ticketInfoDTO.getToStationTelecode()).toString());

		return ticketInfoDTO;
	}

//	https://kyfw.12306.cn/otn/leftTicket/queryTicketPrice?train_no=710000G42208&from_station_no=12&to_station_no=20&seat_types=OM9&train_date=2020-09-09

}
