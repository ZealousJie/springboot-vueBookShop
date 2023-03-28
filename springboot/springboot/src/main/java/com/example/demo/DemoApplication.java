package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.unit.DataSize;

import javax.servlet.MultipartConfigElement;

@SpringBootApplication
@EnableTransactionManagement//开启事务注解
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}


//	/**
//	 * 文件上传配置
//	 * @return
//	 */
//	@Bean
//	public MultipartConfigElement multipartConfigElement() {
//		MultipartConfigFactory factory = new MultipartConfigFactory();
//		// 单个文件最大大小(KB,MB)
//		factory.setMaxFileSize(DataSize.parse("10240KB"));
//		// 上传数据总大小
//		factory.setMaxRequestSize(DataSize.parse("1024MB"));
//		return factory.createMultipartConfig();
//	}

}
