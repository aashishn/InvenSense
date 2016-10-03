package com.invensense.sync.impl;

import java.text.SimpleDateFormat;
import java.util.List;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import com.invensense.model.FinanceForeCast;
import com.invensense.model.FinanceForecastExist;
import com.invensense.model.ForeCast;
import com.invensense.model.LastCopiedTimestamp;
import com.invensense.service.EntityService;
import com.invensense.service.impl.ForecastService;
import com.invensense.sync.SyncFinanceForecast;
import com.invensense.util.Constants;

@Service
public abstract class AbstractSyncFinanceForecast implements SyncFinanceForecast {
	
	private Logger log = Logger.getLogger(AbstractSyncFinanceForecast.class);
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Resource
	private EntityService entityService;
	
	@Resource
	private ForecastService forecastService;

	@Override
	public void syncFinanceForecast(ForeCast salesForecast, Boolean overwriteFuruteMonths) throws Exception {
		if(overwriteFuruteMonths) {
			List<FinanceForeCast> financeForeCastList = entityService.findByNameQuery(Constants.GET_FINANCE_FORECAST_BY_COMPOSITE_KEY, 
														new Object[] { salesForecast.getEndCustomerId(), salesForecast.getProductId(), salesForecast.getYear(),
														salesForecast.getMarket(), salesForecast.getSubMarket(), salesForecast.getProgramName(), salesForecast.getBusinessUnit()});
			if(!financeForeCastList.isEmpty()) {
				FinanceForeCast financeForeCast = financeForeCastList.get(0);
				updateFinanceForecastFutureMonthsFromSalesForecast(financeForeCast, salesForecast);
				persistSalesForecastLastModifiedDate(salesForecast);
			}
		} else {
			List<FinanceForeCast> financeForeCastList = entityService.findByNameQuery(Constants.GET_INVISIBLE_FINANCE_FORECAST_BY_COMPOSITE_KEY, 
														new Object[] { salesForecast.getEndCustomerId(), salesForecast.getProductId(), salesForecast.getYear(),
														salesForecast.getMarket(), salesForecast.getSubMarket(), salesForecast.getProgramName(), salesForecast.getBusinessUnit()});
			if(financeForeCastList.isEmpty()) {
				forecastService.syncFinanceForecastFromHistory(salesForecast);
			} else {
				FinanceForeCast financeForeCast = financeForeCastList.get(0);
				updateFinanceForecastFutureMonthsFromSalesForecast(financeForeCast, salesForecast);
			}
			
			List<FinanceForecastExist> financeForecastExistList = entityService.findByNameQuery(Constants.GET_FINANCE_FORECAST_EXIST_NOT_LOCKED);
			if(financeForecastExistList.isEmpty()) {
				persistSalesForecastLastModifiedDate(salesForecast);
			}
		}
	}
	
	private void persistSalesForecastLastModifiedDate(ForeCast forecast) {
		LastCopiedTimestamp lastCopiedTimestamp = new LastCopiedTimestamp();
		lastCopiedTimestamp.setForecastType(Constants.FORECAST_TYPE_SALES_FORECAST);
		lastCopiedTimestamp.setLastCopiedTimestamp(forecast.getModifiedDate());
		lastCopiedTimestamp.setLastCopiedTimestampString(dateFormat.format(forecast.getModifiedDate()));
		entityService.save(lastCopiedTimestamp);
	}
	
	protected abstract void updateFinanceForecastFutureMonthsFromSalesForecast(FinanceForeCast financeForeCast, ForeCast salesForecast);

}
