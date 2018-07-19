package org.exam.Forum.dao;

import org.exam.Forum.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("UserDao")
public interface UserDao extends JpaRepository<User, Integer> {

	User findByAccount(String account);
}
