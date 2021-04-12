package com.sofi.coupons.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.sofi.coupons.beans.data.CacheLoginData;
import com.sofi.coupons.beans.dto.UserInfoDto;
import com.sofi.coupons.entities.UserEntity;

public interface IUsersDao extends CrudRepository<UserEntity, Long>{

	public boolean existsByUserName(String userName);
	public boolean existsById(long id);
	public boolean existsByIdAndPassword(long id, String password);

	@Query("select new com.sofi.coupons.beans.dto.UserInfoDto(u) from UserEntity u where u.id = ?1")
	public UserInfoDto getUser(long id);

	@Transactional
	@Modifying
	@Query("update UserEntity u set u.userName = ?1, u.firstName = ?2, u.lastName = ?3 where u.id = ?4")
	public void updateUser(String userName, String firstName, String lastName, long userId);
	
	@Transactional
	@Modifying
	@Query("update UserEntity u set u.password=?1 where u.id = ?2")
	public void changePassword(String password, long userId);
	
	@Query("select new com.sofi.coupons.beans.dto.UserInfoDto(u) from UserEntity u")
	public List<UserInfoDto> getAllUsers();

	@Query("select new com.sofi.coupons.beans.data.CacheLoginData(u.id, u.userType, u.company.id) from UserEntity u where u.userName = ?1 and u.password = ?2")
	public CacheLoginData login(String userName, String Password);
}