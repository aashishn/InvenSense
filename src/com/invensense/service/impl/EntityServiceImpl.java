package com.invensense.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.Session;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.service.jdbc.connections.spi.ConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.invensense.service.EntityServiceAbstract;

@Repository
public class EntityServiceImpl extends EntityServiceAbstract {

	@PersistenceContext(name="persistenceUnit")
	private EntityManager entityManager;

	@Autowired(required = true)
	public void setEntityService() {
		super.setEntityManager(entityManager);
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}
	
	//@Transactional("local")
	public Connection getConnection() throws SQLException {
		Session session = (Session) entityManager.getDelegate();
		SessionFactoryImplementor sfi = (SessionFactoryImplementor) session.getSessionFactory();
		ConnectionProvider cp = sfi.getConnectionProvider();
		Connection  c = cp.getConnection();
		return c;
	}

}
