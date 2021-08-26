package cn.zifangsky.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

import java.util.Map;

@Slf4j
public class MyPartition implements Partitioner {
    // 分区数
    private static final Integer PARTITION_NUM = 45;

    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        if (null == key){
            return 0;
        }
//        String keyValue = String.valueOf(key);
        // key取模
        int partitionId = (int) (Long.valueOf(key.toString())%PARTITION_NUM);
        return partitionId;
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> configs) {

    }

}
