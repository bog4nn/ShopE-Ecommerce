package com.shope.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shope.common.entity.Role;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class RoleRepositoryTests {
	
	@Autowired
	private RoleRepository repo;
	
	@Test
	public void testCreateFirstRole() {
		Role roleAdmin = new Role("Admin","manage entire operation");
		Role saveRole = repo.save(roleAdmin);
		
		assertThat(saveRole.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateRestRole() {
		Role roleSaleperson = new Role("Salesperson","manage product price, customer , shipping, "
				+ "order and sales report");
		Role roleEditor = new Role("Editor","manage categories , brand ,"
				+ "product, articles and menus");
		Role roleShipper = new Role("Shipper" , "view products, view orders and update order status");
		
		Role roleAssistant = new Role("Assistant", "manage question and reviews");
		
		repo.saveAll(List.of(roleSaleperson,roleEditor,roleShipper,roleAssistant));
	}
}
