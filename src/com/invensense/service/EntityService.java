package com.invensense.service;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.exception.DataException;

import com.invensense.model.GenericModel;

public interface EntityService {

	public abstract void setEntityManager(EntityManager em);
	public abstract void setEntityManager(String name);
	public abstract void save(Object o);
	public abstract void delete(Object o);
	
	public abstract List findByNameQuery(String namedQuery) throws DataException;
	public abstract List findByNameQuery(String namedQuery, Object[] values) throws DataException;
	public abstract List findByQuery(String query) throws DataException;
	public abstract List findByQuery(String query, Object[] values) throws DataException;
	public abstract <T extends GenericModel> T get(Class clazz, Serializable id) throws DataException;
	public abstract EntityManager getEntityManager();
	public abstract Connection getConnection() throws SQLException;

}