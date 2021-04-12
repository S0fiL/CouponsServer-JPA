package com.sofi.coupons.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.sofi.coupons.beans.dto.CompanyDto;
import com.sofi.coupons.entities.CompanyEntity;

public interface ICompaniesDao extends CrudRepository<CompanyEntity, Long>{
	
	public boolean existsByName(String name);
	public boolean existsByEmail(String email);
	public boolean existsById(long id);
	
	@Query("select new com.sofi.coupons.beans.dto.CompanyDto(c) from CompanyEntity c where c.id = ?1")
	public CompanyDto getCompany(long id);
	
	@Query("select new com.sofi.coupons.beans.dto.CompanyDto(c) from CompanyEntity c")
    public List<CompanyDto> getAllCompanies();
}