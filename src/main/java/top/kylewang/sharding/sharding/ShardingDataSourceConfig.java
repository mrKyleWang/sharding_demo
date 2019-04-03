package top.kylewang.sharding.sharding;

import com.alibaba.druid.pool.DruidDataSource;
import io.shardingsphere.api.config.rule.ShardingRuleConfiguration;
import io.shardingsphere.api.config.rule.TableRuleConfiguration;
import io.shardingsphere.api.config.strategy.StandardShardingStrategyConfiguration;
import io.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author KyleWang
 * @version 1.0
 * @date 2019年03月19日
 */
@Configuration
public class ShardingDataSourceConfig {

	@Bean(name = "shardingJdbcTemplate")
	public JdbcTemplate jdbcTemplate(@Qualifier("shardingDataSource") DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

	/**
	 * 配置事务管理器
	 * @param dataSource 数据源
	 * @param transactionManagerCustomizers Customizers
	 * @return
	 */
	@Bean(name = "shardingTransactionManager")
	public PlatformTransactionManager shardingTransactionManager(@Qualifier("shardingDataSource") DataSource dataSource,
			ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers) {
		DataSourceTransactionManager result = new DataSourceTransactionManager(dataSource);
		result.setDefaultTimeout(3000);
		if (null != transactionManagerCustomizers.getIfAvailable()) {
			((TransactionManagerCustomizers) transactionManagerCustomizers.getIfAvailable()).customize(result);
		}
		return result;
	}

    /**
     * 分片数据源
     * @return dataSource
     * @throws SQLException
     */
	@Bean(name = "shardingDataSource")
	public DataSource dataSource() throws SQLException {
		// 配置真实数据源
		Map<String, DataSource> dataSourceMap = new HashMap<>();

		// 配置第一个数据源
		DruidDataSource dataSource0 = new DruidDataSource();
		dataSource0.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource0.setUrl("jdbc:mysql://localhost:3306/sharding0?useUnicode=true&characterEncoding=utf8");
		dataSource0.setUsername("root");
		dataSource0.setPassword("rootpass");
		dataSourceMap.put("ds0", dataSource0);

		// 配置第二个数据源
		DruidDataSource dataSource1 = new DruidDataSource();
		dataSource1.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource1.setUrl("jdbc:mysql://localhost:3306/sharding1?useUnicode=true&characterEncoding=utf8");
		dataSource1.setUsername("root");
		dataSource1.setPassword("rootpass");
		dataSourceMap.put("ds1", dataSource1);

		// 配置第一个数据源
		DruidDataSource dataSource2 = new DruidDataSource();
		dataSource2.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource2.setUrl("jdbc:mysql://localhost:3306/sharding2?useUnicode=true&characterEncoding=utf8");
		dataSource2.setUsername("root");
		dataSource2.setPassword("rootpass");
		dataSourceMap.put("ds2", dataSource2);

		// 配置第二个数据源
		DruidDataSource dataSource3 = new DruidDataSource();
		dataSource3.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource3.setUrl("jdbc:mysql://localhost:3306/sharding3?useUnicode=true&characterEncoding=utf8");
		dataSource3.setUsername("root");
		dataSource3.setPassword("rootpass");
		dataSourceMap.put("ds3", dataSource3);

		// 配置user表规则
		TableRuleConfiguration tableRuleConfiguration = new TableRuleConfiguration();
		tableRuleConfiguration.setLogicTable("user");
		tableRuleConfiguration
				.setActualDataNodes("ds0.user_${0..7},ds1.user_${8..15},ds2.user_${16..23},ds3.user_${24..31}");

		// 配置分库 + 分表策略
		tableRuleConfiguration.setDatabaseShardingStrategyConfig(new StandardShardingStrategyConfiguration("userid",
				new ShardingDatabaseAlgorithmConfig(), new ShardingDatabaseAlgorithmConfig()));
		tableRuleConfiguration.setTableShardingStrategyConfig(new StandardShardingStrategyConfiguration("userid",
				new ShardingTableAlgorithmConfig(), new ShardingDatabaseAlgorithmConfig()));

		// 配置分片规则
		ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
		shardingRuleConfig.getTableRuleConfigs().add(tableRuleConfiguration);

		// 获取数据源对象
		return ShardingDataSourceFactory.createDataSource(dataSourceMap, shardingRuleConfig, new ConcurrentHashMap<>(),
				new Properties());
	}
}
