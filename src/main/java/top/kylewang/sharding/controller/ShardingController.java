package top.kylewang.sharding.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.kylewang.sharding.dao.ShardingDao;
import top.kylewang.sharding.entity.User;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author KyleWang
 * @version 1.0
 * @date 2019年03月05日
 */
@RestController
public class ShardingController {

	@Autowired
	private ShardingDao shardingDao;

	@RequestMapping("query")
	public User query(int userid) {
		return shardingDao.query(userid);
	}

	@RequestMapping("queryByTime")
	public List<User> queryByTime(String start, String end) {
		return shardingDao.queryByTime(start, end);
	}

	@RequestMapping("queryByList")
	public List<User> queryByList(String ids) {
		String[] split = ids.split(",");
		List<String> list = Arrays.asList(split);
		List<Integer> idList = list.stream().map(Integer::parseInt).collect(Collectors.toList());
		return shardingDao.queryByList(idList);
	}

	@RequestMapping("insert")
	public void query(int userid, String name, String phone) {
		shardingDao.insert(userid, name, phone);
	}
}
