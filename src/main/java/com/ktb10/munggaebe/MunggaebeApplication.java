package com.ktb10.munggaebe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MunggaebeApplication {

	public static void main(String[] args) {
		SpringApplication.run(MunggaebeApplication.class, args);
	}

}
