package top.kylewang.sharding.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import top.kylewang.sharding.entity.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author KyleWang
 * @version 1.0
 * @date 2019年03月05日
 */
@Repository
public class ShardingDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private RowMapper<User> userRow = (rs, rowNum) -> {
		User user = new User();
		user.setId(rs.getInt("id"));
		user.setUserid(rs.getInt("userid"));
		user.setName(rs.getString("name"));
		user.setPhone(rs.getString("phone"));
		user.setCreateTime(rs.getTimestamp("createTime"));
		return user;
	};

	public User query(int userid) {
		String sql = "SELECT * FROM user WHERE userid = ?";
		List<Object> args = new ArrayList<>();
		args.add(userid);
		try {
			return jdbcTemplate.queryForObject(sql, args.toArray(), userRow);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public void insert(int userid, String name, String phone) {
		String sql = "INSERT INTO user(`userid`, `name`, `phone`, `createTime`) VALUES (?, ?, ?, NOW());";
		List<Object> args = new ArrayList<>();
		args.add(userid);
		args.add(name);
		args.add(phone);
		jdbcTemplate.update(sql, args.toArray());
	}

	public List<User> queryByTime(String start, String end) {
		String sql = "SELECT * FROM user WHERE `createTime` BETWEEN ? AND ?";
		List<Object> args = new ArrayList<>();
		args.add(start);
		args.add(end);
		try {
			return jdbcTemplate.query(sql, args.toArray(), userRow);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<User> queryByList(List<Integer> list) {
		HashMap<String, Object> params = new HashMap<>();
		String sql = "SELECT * FROM user WHERE `userid` IN (:ids)";
		params.put("ids", list);
		try {
			return new NamedParameterJdbcTemplate(jdbcTemplate).query(sql, params, userRow);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
}
