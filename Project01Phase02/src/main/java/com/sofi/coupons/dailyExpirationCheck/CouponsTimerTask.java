package com.sofi.coupons.dailyExpirationCheck;

import java.sql.Date;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sofi.coupons.dao.ICouponsDao;
import com.sofi.coupons.enums.ExceptionType;
import com.sofi.coupons.exceptions.ApplicationException;

@Component
public class CouponsTimerTask {

	@Autowired
	private ICouponsDao couponsDao;

	@Scheduled(cron = "0 0 0 * * *")
	public void dailyExpirationCheck() throws ApplicationException {
		System.out.println("schedule task STARTED");
		Calendar cal = Calendar.getInstance();
		Date expirationDate = new Date(cal.getTimeInMillis());
		try {
			couponsDao.deleteAllByEndDateBefore(expirationDate);
		} catch (Exception e){
			throw new ApplicationException(e, ExceptionType.GENERAL_ERROR, "Failed to delete expired coupons by date: "+expirationDate);
		}
		System.out.println("schedule task ENDED");
	}
}
