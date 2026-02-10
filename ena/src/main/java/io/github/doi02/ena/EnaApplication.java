package io.github.doi02.ena;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.sql.DataSource; // <--- 이거!
import java.sql.Connection;

@SpringBootApplication
@EnableJpaAuditing
public class EnaApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnaApplication.class, args);
    }

    @Bean
    public CommandLineRunner testConnection(DataSource dataSource) {
        return args -> {
            try (Connection conn = dataSource.getConnection()) {
                System.out.println("------------------------------------");
                System.out.println("!!!!! 실제 접속 정보 !!!!!");
                System.out.println("접속 URL: " + conn.getMetaData().getURL());
                System.out.println("접속 유저: " + conn.getMetaData().getUserName());
                System.out.println("데이터베이스 버전: " + conn.getMetaData().getDatabaseProductVersion());
                System.out.println("------------------------------------");
            } catch (Exception e) {
                System.out.println("접속 확인 중 에러 발생: " + e.getMessage());
            }
        };
    }
}