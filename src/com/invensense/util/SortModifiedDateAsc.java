package com.invensense.util;

import java.util.Comparator;
import org.springframework.stereotype.Component;
import com.invensense.model.BaseModel;

@Component
public class SortModifiedDateAsc implements Comparator<BaseModel> {

	@Override
	public int compare(BaseModel o1, BaseModel o2) {
		if (o1.getCrmodModifiedDate() == null) {
			return -1 ;
		}
		if (o2.getCrmodModifiedDate() == null) {
			return 1 ;
		}
		return o1.getCrmodModifiedDate().compareTo(o2.getCrmodModifiedDate());
	}

}
