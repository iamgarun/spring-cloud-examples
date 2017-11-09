package com.example.reservations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Random;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.RequestMapping;

@EnableDiscoveryClient
@SpringBootApplication
public class ReservationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReservationServiceApplication.class, args);
	}

	@Bean
	CommandLineRunner resrvationDemo(ReservationRespository rr) {

		return arg0 -> {
			Random rand = new Random();
			Arrays.asList("Arun", "Kumar", "Adra", "Sakka").forEach(e -> {
				rr.save(new Reservation(rand.nextInt(100), e));
			});

		};
	}
	
}

@RefreshScope
@org.springframework.web.bind.annotation.RestController
class RestController{

	@Value("${message}")
	private String message;
	
	@RequestMapping("/message")
	String message(){
		return message;
	}
}

@RepositoryRestResource
interface ReservationRespository extends JpaRepository<Reservation, BigDecimal> {

}

@Entity
class Reservation {

	@Id
	@GeneratedValue
	private BigDecimal id;

	private int resrvationCount;

	private String reservetionFor;

	public Reservation() {
		// JPA Expects default constructor to be available
		super();
	}

	public Reservation(int resrvationCount, String reservetionFor) {
		super();
		this.resrvationCount = resrvationCount;
		this.reservetionFor = reservetionFor;
	}

	public BigDecimal getId() {
		return id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	public int getResrvationCount() {
		return resrvationCount;
	}

	public void setResrvationCount(int resrvationCount) {
		this.resrvationCount = resrvationCount;
	}

	public String getReservetionFor() {
		return reservetionFor;
	}

	public void setReservetionFor(String reservetionFor) {
		this.reservetionFor = reservetionFor;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Reservation [id=").append(id).append(", resrvationCount=").append(resrvationCount)
				.append(", reservetionFor=").append(reservetionFor).append("]");
		return builder.toString();
	}

}