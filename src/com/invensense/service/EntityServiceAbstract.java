package com.invensense.service;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.exception.DataException;
import org.springframework.transaction.annotation.Transactional;

import com.invensense.model.BaseModel;
import com.invensense.model.GenericModel;

public  abstract class EntityServiceAbstract implements EntityService {

	private EntityManager entityManager;
	
	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

	/* (non-Javadoc)
	 * @see com.crmod.service.EntityService#save(T)
	 */
	@Override
	@Transactional
	public  void save(Object o) {
		o = entityManager.merge(o);
	}
	
	/* (non-Javadoc)
	 * @see com.crmod.service.EntityService#delete(T)
	 */
	@Override
	@Transactional
	public  void delete(Object o) {
		entityManager.remove(o);
	}
	
	/* (non-Javadoc)
	 * @see com.crmod.service.EntityService#findByNameQuery(java.lang.String)
	 */
	@Override
	@Transactional()
	public List findByNameQuery(String namedQuery) throws DataException  {
		return findByNameQuery(namedQuery,null);
	}
	
	/* (non-Javadoc)
	 * @see com.crmod.service.EntityService#findByNameQuery(java.lang.String, java.lang.Object[])
	 */
	@Override
	@Transactional
	public List findByNameQuery(String namedQuery,Object[] values) throws DataException {
		Query q = entityManager.createNamedQuery(namedQuery);
	    if (values != null) {
	        for (int i = 0; i < values.length; i++) {
	            q.setParameter(i + 1, values[i]);
	        }
	    }
	    return q.getResultList();
	}
	
	/* (non-Javadoc)
	 * @see com.crmod.service.EntityService#get(java.lang.Class, java.io.Serializable)
	 */
	@Override
	@Transactional
	public <T extends GenericModel> T  get(Class clazz,Serializable id) throws DataException  {
		return (T) entityManager.find(clazz, id);
	}

	@Override
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public void setEntityManager(String name) {
		
	}

	@Override
	public List findByQuery(String query) throws DataException {
		return findByQuery(query, null);
	}

	@Override
	public List findByQuery(String namedQuery, Object[] values)	throws DataException {
		Query q = entityManager.createQuery(namedQuery);
	    if (values != null) {
	        for (int i = 0; i < values.length; i++) {
	            q.setParameter(i + 1, values[i]);
	        }
	    }
	    return q.getResultList();
	}
}
