package top.kylewang.sharding.sharding;

import io.shardingsphere.api.algorithm.sharding.PreciseShardingValue;
import io.shardingsphere.api.algorithm.sharding.standard.PreciseShardingAlgorithm;

import java.util.Collection;

/**
 * @author KyleWang
 * @version 1.0
 * @date 2019年03月18日
 */
public class ShardingDatabaseAlgorithmConfig implements PreciseShardingAlgorithm<Integer> {

	@Override
	public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Integer> shardingValue) {
		for (String availableTargetName : availableTargetNames) {
			Integer userid = shardingValue.getValue();
			int suffix = (userid % 32) / 8;
			String dataSourceName = "ds" + suffix;
			if (availableTargetName.equalsIgnoreCase(dataSourceName)) {
				return availableTargetName;
			}
		}
		return null;
	}
}
