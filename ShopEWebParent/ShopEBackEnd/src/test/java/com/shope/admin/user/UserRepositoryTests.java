package com.shope.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import com.shope.common.entity.Role;
import com.shope.common.entity.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {
	
	@Autowired
	private UserRepository repo;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCreateUserWithOneRole() {
		Role roleAdmin = entityManager.find(Role.class, 1);
		User userLongpt = new User("longpt35@gmail.com","longdn123","Long","Phi Thanh");
		userLongpt.addRole(roleAdmin);
		
		User saveUser = repo.save(userLongpt);
		
		assertThat(saveUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateUserWithTwoRole() {
		User userBogan = new User("bog4n@gmail.com","longdn123","bogan","Mamo");
		Role roleEditor = new Role(3);
		Role roleAssistant = new Role(5);
		
		userBogan.addRole(roleEditor);
		userBogan.addRole(roleAssistant);
		User saveUser = repo.save(userBogan);
		
		assertThat(saveUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListAllUser() {
		Iterable<User> listUsers = repo.findAll();
		listUsers.forEach(user -> System.out.println(user));
	}
	
	@Test
	public void testGetUserById() {
		User userLong = repo.findById(1).get();
		System.out.println(userLong);
		assertThat(userLong).isNotNull();
	}
	
	@Test
	public void testUpdateUserDetails() {
		User userLong = repo.findById(1).get();
		userLong.setEnabled(true);
		userLong.setEmail("Longptgcd17310@gmail.com");
		
		repo.save(userLong);
	}
	
	@Test
	public void testUpdateUserRoles() {
		User userBogan = repo.findById(2).get();
		Role roleEditor = new Role(3);
		Role roleSalesperson = new Role(2);
		userBogan.getRoles().remove(roleEditor);
		userBogan.addRole(roleSalesperson);
		
		repo.save(userBogan); 
	}
	
	@Test
	public void testDeleteUser() {
		Integer userId = 2;
		repo.deleteById(userId);
	}
	
	@Test
	public void testGetUserByEmail(){
		String email = "longphi1234@gmail.com";
		User user = repo.getUserByEmail(email);
		
		assertThat(user).isNotNull();
	}
	
	@Test
	public void testCountById() {
		Integer id = 1;
		Long countById = repo.countById(id);
		
		assertThat(countById).isNotNull().isGreaterThan(0);
	}
	
	@Test
	public void testDisableUser() {
		Integer id = 11;
		repo.updateEnabledStatus(id, false);
	}
	
	@Test
	public void testEnableUser() {
		Integer id = 11;
		repo.updateEnabledStatus(id, true);
	}
	
	@Test
	public void testListFirstPage() {
		int pageNumber = 0;
		int pageSize = 4;
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		
		Page<User> page =repo.findAll(pageable);
		
		List<User> listUsers =page.getContent();
		for (User user : listUsers) {
			System.out.println(user);
		}
		
		assertThat(listUsers.size()).isEqualTo(pageSize);
	}
	
	@Test
	public void testSearchUser() {
		String keyword = "bruce";
		
		int pageNumber = 0;
		int pageSize = 4;
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		
		Page<User> page =repo.findAll(keyword,pageable);
		
		List<User> listUsers =page.getContent();
		for (User user : listUsers) {
			System.out.println(user);
		}
		
		assertThat(listUsers.size()).isGreaterThan(0);
	}
}
