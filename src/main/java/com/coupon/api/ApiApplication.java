package com.coupon.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

// main em api; beans e entidades em coupon.* — por isso scan explícito
@ComponentScan(basePackages = "com.coupon")
@EnableJpaRepositories(basePackages = "com.coupon.coupon.infrastructure.persistence")
@EntityScan(basePackages = "com.coupon.coupon.infrastructure.persistence")
@SpringBootApplication
public class ApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
		System.out.println("- API started - ");
	}

}
