package com.sofi.coupons.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sofi.coupons.beans.dto.CompanyDto;
import com.sofi.coupons.exceptions.ApplicationException;
import com.sofi.coupons.logic.CompaniesController;

@RestController
@RequestMapping("/companies")
public class CompaniesApi {

	@Autowired
	private CompaniesController companiesController;

	@PostMapping
	public long createCompany(@RequestBody CompanyDto company) throws ApplicationException {
		long companyId = companiesController.createCompany(company);
		System.out.println(companyId);
		return companyId;
	}

	@GetMapping("/{companyId}")
	public CompanyDto getCompany(@PathVariable("companyId") long id) throws ApplicationException {
		return companiesController.getCompany(id);
	}

	@PutMapping
	public void updateCompany(@RequestBody CompanyDto company) throws ApplicationException {
		companiesController.updateCompany(company);
	}

	@DeleteMapping("/{companyId}")
	public void deleteCompany(@PathVariable("companyId") long id) throws ApplicationException {
		companiesController.deleteCompany(id);
	}

	@GetMapping
	public List<CompanyDto> getAllCompanies() throws ApplicationException {
		return companiesController.getAllCompanies();
	}
}