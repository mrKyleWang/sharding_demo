package top.kylewang.sharding.service;

import io.shardingsphere.transaction.annotation.ShardingTransactionType;
import io.shardingsphere.transaction.api.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.kylewang.sharding.dao.UserDao;
import top.kylewang.sharding.entity.User;

import java.util.List;

/**
 * @author KyleWang
 * @version 1.0
 * @date 2019年03月19日
 */
@Service
public class UserService {

	@Autowired
	private UserDao userDao;

	public User query(int userid) {
		return userDao.query(userid);
	}

	public List<User> queryByTime(String start, String end) {
		return userDao.queryByTime(start, end);
	}

	public List<User> queryByList(List<Integer> idList) {
		return userDao.queryByList(idList);
	}

	public boolean insert(int userid, String name, String phone) {
		return userDao.insert(userid, name, phone);
	}

	@ShardingTransactionType(TransactionType.XA)
	@Transactional
	public boolean txBatchInsert(List<User> userList) {
		for (User user : userList) {
			userDao.insert(user.getUserid(), user.getName(), user.getPhone());
		}
		return true;
	}
}
