package com.sofi.coupons.filters;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.sofi.coupons.beans.data.CacheLoginData;
import com.sofi.coupons.logic.CacheController;

@Component
@Order(2)
public class AuthorizationFilter implements Filter{

	@Autowired
	private CacheController cacheController;

	@Override
	public void doFilter (ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		//Casting to httpServlet for future use
		HttpServletRequest httpRequest = (HttpServletRequest)request;

		//Getting the URL to check the request type
		String requestUrl = httpRequest.getRequestURL().toString();
		String method = httpRequest.getMethod().toString();

		//Checking if the user logged in
		String token = httpRequest.getHeader("authorization");
		CacheLoginData cacheLoginData = null;
		if(token != null) {
		cacheLoginData = (CacheLoginData) cacheController.getData(token);
		}
		//If the user is not logged in yet the only methods available are login and create new user
		if(cacheLoginData == null && (requestUrl.contains("/users") && method.equals("POST"))) {
			chain.doFilter(request, response);
			return;
		} 
		switch (cacheLoginData.getUserType()) {
		case ADMIN:

			//An admin has access to all companies' information and is the only one who can change it
			if(requestUrl.contains("/companies")) {
				chain.doFilter(request, response);
				return;
			}
			//Admin has access to all coupons' information (aside from "byCompany" method for companies only and "byMaxPrice" method for users only)
			//But only company employees can change it
			Pattern p = Pattern.compile("/coupons(?!/byCompany+$)(?!/byMaxPrice)");
			Matcher m = p.matcher(requestUrl);
			if(m.find() && method.equals("GET")) {
				chain.doFilter(request, response);
				return;
			}
			//Admin has access to all purchases info (aside from methods for companies and users only) and can delete them
			//But only customers can make purchases
			p = Pattern.compile("/purchases(?!/byCompany+$)(?!/byUser+$)");
			m = p.matcher(requestUrl);
			if(m.find() && (method.equals("GET") || method.equals("DELETE"))) {
				chain.doFilter(request, response);
				return;
			}
			//Admin has access to all users' information, can create new users (admins/company users)and delete them
			//Can update his account only and no user can make a login while signed in
			if(requestUrl.contains("/users") && !requestUrl.contains("/login")) {
				//If an admin creates a user directly after a successful login, need to save the login data for future use
				if(method.equals("POST")) {
					request.setAttribute("cacheLoginData", cacheLoginData);
				}
				chain.doFilter(request, response);
				return;
			}

			break;

		case COMPANY:

			//No users other than admins have any access to companies' data
			
			//Company employees have access to information of all coupons and are the only ones who can make changes in the coupons of the company they work for
			//Since they can't make purchases they don't have access to the "byMaxPrice" method and "forAdmin" method
			p = Pattern.compile("/coupons(?!/byCompanyForAdmin)(?!/byMaxPrice)");
			m = p.matcher(requestUrl);
			if(m.find()) {
				chain.doFilter(request, response);
				return;
			}
			//Company employees can't delete purchases and have access to purchases made from their company only
			p = Pattern.compile("/purchases/([\\d]+$|byCompany$)");
			m = p.matcher(requestUrl);
			if(m.find() && method.equals("GET")) {
				chain.doFilter(request, response);
				return;
			}
			//Company employees can update, delete and get info of their own account only
			p = Pattern.compile("/users/(password$|myAccount$|logOut$)");
			m = p.matcher(requestUrl);
			if(requestUrl.endsWith("/users") && (method.equals("PUT") || method.equals("DELETE")) || m.find()) {
				chain.doFilter(request, response);
				return;
			}

			break;

		case CUSTOMER:

			//No users other than admins have any access to companies' data
			
			//Customers can access all information about companies' coupons (aside from "byCompany" method)
			p = Pattern.compile("/coupons(?!/byCompany)");
			m = p.matcher(requestUrl);
			if(m.find() && method.equals("GET")) {
				chain.doFilter(request, response);
				return;
			}
			//Customers can make purchases and access information about their purchases only
			p = Pattern.compile("/purchases/([\\d]+$|byUser$)");
			m = p.matcher(requestUrl);
			if(requestUrl.endsWith("/purchases") && method.equals("POST") || (m.find() && method.equals("GET"))) {
				chain.doFilter(request, response);
				return;
			}
			//Customers can update, delete and get information about their own account only
			p = Pattern.compile("/users/(password$|myAccount$|logOut$)");
			m = p.matcher(requestUrl);
			if(requestUrl.endsWith("/users") && (method.equals("PUT") || method.equals("DELETE")) || m.find()) {
				chain.doFilter(request, response);
				return;
			}
		}

		//If the request did not match any condition above it's denied
		HttpServletResponse httpResponse = (HttpServletResponse)response;
		httpResponse.setStatus(401);
	}


	@Override
	public void init(FilterConfig arg0) throws ServletException{
	}
}