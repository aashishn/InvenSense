package com.invensense.util;

import java.util.Comparator;
import org.springframework.stereotype.Component;
import com.invensense.model.BaseModel;

@Component
public class SortCreatedDateAsc implements Comparator<BaseModel> {

	@Override
	public int compare(BaseModel o1, BaseModel o2) {
		if (o1.getCrmodCreatedDate() == null) {
			return -1 ;
		}
		if (o2.getCrmodCreatedDate() == null) {
			return 1 ;
		}
		return o1.getCrmodCreatedDate().compareTo(o2.getCrmodCreatedDate());
	}

}
