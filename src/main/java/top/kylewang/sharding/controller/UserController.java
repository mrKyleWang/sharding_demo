package top.kylewang.sharding.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.kylewang.sharding.entity.User;
import top.kylewang.sharding.service.UserService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author KyleWang
 * @version 1.0
 * @date 2019年03月05日
 */
@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@RequestMapping("query")
	public User query(int userid) {
		return userService.query(userid);
	}

	@RequestMapping("queryByTime")
	public List<User> queryByTime(String start, String end) {
		return userService.queryByTime(start, end);
	}

	@RequestMapping("queryByList")
	public List<User> queryByList(String ids) {
		String[] split = ids.split(",");
		List<String> list = Arrays.asList(split);
		List<Integer> idList = list.stream().map(Integer::parseInt).collect(Collectors.toList());
		return userService.queryByList(idList);
	}

	@RequestMapping("insert")
	public boolean insert(int userid, String name, String phone) {
		return userService.insert(userid, name, phone);
	}

	@RequestMapping("batchInsert")
	public boolean batchInsert(@RequestBody List<User> userList) {
		return userService.txBatchInsert(userList);
	}
}
