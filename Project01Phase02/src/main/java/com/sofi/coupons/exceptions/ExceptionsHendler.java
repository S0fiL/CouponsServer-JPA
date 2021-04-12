package com.sofi.coupons.exceptions;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.sofi.coupons.beans.ExceptionBean;

@RestControllerAdvice
public class ExceptionsHendler {

	@ExceptionHandler
	@ResponseBody
	public ExceptionBean toResponse(Throwable throwable, HttpServletResponse response) {
		if(throwable instanceof ApplicationException) {
			ApplicationException applicationException = (ApplicationException)throwable;
			int exceptionNum = applicationException.getExceptionType().getExceptionNumber();
			ExceptionBean exceptionBean = new ExceptionBean(exceptionNum, applicationException.getExceptionType().toString().replace("_", " "), applicationException.getExceptionType().getMessage());
			response.setStatus(exceptionNum);
			if(applicationException.getExceptionType().isPrintStackTrace()) {
				applicationException.printStackTrace();
			}
			if(applicationException.getExceptionType().isUrgent()) {
				System.out.println("Atention! Atempt at accessesing restricted data");
			}
			return exceptionBean;
		}
		response.setStatus(600);
		ExceptionBean exceptionBean = new ExceptionBean(601, "General Error", throwable.getMessage());
		throwable.printStackTrace();
		return exceptionBean;
	}
}