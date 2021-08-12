package com.zkml.api.warning;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(basePackages = "com.zkml.api.warning.dao")
@SpringBootApplication
public class WarningApplication {

	public static void main(String[] args) {
		SpringApplication.run(WarningApplication.class, args);
	}

}
