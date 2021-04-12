package com.sofi.coupons.filters;

import java.io.IOException;

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
@Order(1)
public class LoginFilter implements Filter {

	@Autowired
	private CacheController cacheController;

	@Override
	public void doFilter (ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		//Casting to httpServlet for future use
		HttpServletRequest httpRequest = (HttpServletRequest)request;

		//Getting the URL to check the request type
		String requestUrl = httpRequest.getRequestURL().toString();

		//If it's a "Login" request there's no need for token verification
		if(requestUrl.endsWith("/login")) {
			chain.doFilter(request, response);
			return;
		}

		//If it's a "Sign up" request there's no need for token verification
		if(requestUrl.endsWith("/users") && httpRequest.getMethod().toString().equals("POST")) {
			chain.doFilter(request, response);
			return;
		}

		//For every other request, verifying that the token matches to another one in the cache
		String token = httpRequest.getHeader("authorization");
		CacheLoginData cacheLoginData = null;
		if(token != null) {
		cacheLoginData = (CacheLoginData) cacheController.getData(token);
		}
		//If there's a match, the user is logged in and his request is granted
		//Saving the login data we got from the cache for further use
		if(cacheLoginData != null) {
			request.setAttribute("cacheLoginData", cacheLoginData);
			chain.doFilter(request, response);
			return;
		}
		//If the token didn't match the user is not logged in, so his request is denied
		HttpServletResponse httpResponse = (HttpServletResponse)response;
		httpResponse.setStatus(401);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException{
	}
}