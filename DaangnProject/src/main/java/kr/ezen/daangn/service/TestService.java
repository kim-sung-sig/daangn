package kr.ezen.daangn.service;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.ezen.daangn.dao.TestDAO;

@Service
@Transactional
public class TestService {
	@Autowired
	TestDAO testDAO;
	
	public String selectToday() {
		String result = null;
		try {
			result = testDAO.selectToday();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
}
