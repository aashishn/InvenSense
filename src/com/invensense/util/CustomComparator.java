/**
 * 
 */
package com.invensense.util;

import java.util.Comparator;

import com.invensense.model.BaseModel;

/**
 * @author NagRaj
 *
 */
public class CustomComparator implements Comparator<BaseModel> {
    @Override
    public int compare(BaseModel o1, BaseModel o2) {
        return o1.getCrmodModifiedDate().compareTo(o2.getCrmodModifiedDate());
    }
    
}
