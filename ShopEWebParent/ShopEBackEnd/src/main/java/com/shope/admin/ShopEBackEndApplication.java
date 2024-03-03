package com.shope.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.shope.common.entity","com.shope.admin.user"})
public class ShopEBackEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopEBackEndApplication.class, args);
	}

}
