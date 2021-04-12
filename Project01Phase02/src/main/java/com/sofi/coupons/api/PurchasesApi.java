package com.sofi.coupons.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sofi.coupons.beans.data.CacheLoginData;
import com.sofi.coupons.beans.dto.PurchaseDto;
import com.sofi.coupons.beans.dto.PurchaseInfoDto;
import com.sofi.coupons.exceptions.ApplicationException;
import com.sofi.coupons.logic.PurchasesController;

@RestController
@RequestMapping("/purchases")
public class PurchasesApi {

	@Autowired
	private PurchasesController purchasesController;

	@PostMapping
	public long createPurchase(@RequestBody PurchaseDto purchase , HttpServletRequest request) throws ApplicationException {
		CacheLoginData cacheLoginData = (CacheLoginData) request.getAttribute("cacheLoginData");
		purchase.setUserId(cacheLoginData.getUserId());
		long purchaseId = purchasesController.createPurchase(purchase);
		return purchaseId;
	}

	@GetMapping("/{purchaseId}")
	public PurchaseInfoDto getPurchase(@PathVariable("purchaseId") long id, HttpServletRequest request) throws ApplicationException {
		return purchasesController.getPurchase(id, request);
	}

	@DeleteMapping("/{purchaseId}")
	public void deletePurchase(@PathVariable("purchaseId") long id) throws ApplicationException {
		purchasesController.deletePurchase(id);
	}

	@GetMapping
	public List<PurchaseDto> getAllPurchases() throws ApplicationException {
		return purchasesController.getAllPurchases();
	}

	@GetMapping("/byCompany")
	public List<PurchaseDto> getPurchasesByCompany(HttpServletRequest request) throws ApplicationException {
		CacheLoginData cacheLoginData = (CacheLoginData) request.getAttribute("cacheLoginData");
		return purchasesController.getPurchasesByCompany(cacheLoginData.getCompanyId());
	}

	@GetMapping("/byCompanyForAdmin")
	public List<PurchaseDto> getPurchasesByCompany(@RequestParam long companyId) throws ApplicationException {
		return purchasesController.getPurchasesByCompany(companyId);
	}

	@GetMapping("/byUser")
	public List<PurchaseInfoDto> getPurchasesByUser(HttpServletRequest request) throws ApplicationException {
		CacheLoginData cacheLoginData = (CacheLoginData) request.getAttribute("cacheLoginData");
		return purchasesController.getPurchasesByUser(cacheLoginData.getUserId());
	}

	@GetMapping("/byUserForAdmin")
	public List<PurchaseInfoDto> getPurchasesByUser(@RequestParam long userId) throws ApplicationException {
		return purchasesController.getPurchasesByUser(userId);
	}
}