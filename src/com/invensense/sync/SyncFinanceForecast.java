package com.invensense.sync;

import com.invensense.model.FinanceForeCastHistory;
import com.invensense.model.ForeCast;

public interface SyncFinanceForecast {
	
	public void syncFinanceForecast(ForeCast salesForecast, Boolean overwriteFuruteMonths) throws Exception;
	
	public void syncFinanceForecastFromHistory(ForeCast salesForecast, FinanceForeCastHistory financeForeCastHistory) throws Exception;

}
