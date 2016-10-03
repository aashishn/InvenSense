package com.test.invensense;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.invensense.model.Account;
import com.invensense.service.EntityService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/META-INF/spring/applicationContext.xml")
@Transactional
public class EntityCRUDTestCase {
	
	@Resource
	private EntityService entityService ;
	
	@Test
	public void crud() {
		try {
			// save object
			Account account = new Account();
			account.setId("AAA-BBB");
			account.setName("Test Account");
			entityService.save(account);
			
			// query object 
			account = entityService.get(Account.class, account.getId());
			Assert.assertNotNull(account);
			
			// update object 
			account.setName(account.getName() + "- Update");
			entityService.save(account);
			
			// delete object
			entityService.delete(account);
			
		} catch (Exception e) {
			Assert.assertFalse(false);
		}
	}
}
