package com.example.reservation;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

@EnableZuulProxy
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class ReservationClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReservationClientApplication.class, args);
	}

	@LoadBalanced // Creates a cloud aware rest template (with round robin load balancing?)
	@Bean
	RestTemplate restTemplate(RestTemplateBuilder rtb) {
		return rtb.build();
	}

	@Bean
	CommandLineRunner getInstances(DiscoveryClient dc) {
		return args -> {
			Thread.sleep(15000);
			dc.getInstances("eureka.reservation-service").forEach(System.out::println);
		};
	}

	@Bean
	CommandLineRunner callWithRestTemplate(RestTemplate rt) {

		ParameterizedTypeReference<org.springframework.hateoas.Resource<List<Reservation>>> ptr = 
				new ParameterizedTypeReference<org.springframework.hateoas.Resource<List<Reservation>>>() {
		};

		return args -> {
			ResponseEntity<Resource<List<Reservation>>> t = rt.exchange("http://eureka.reservation-service/reservations", HttpMethod.GET, null, ptr);
			System.out.println(t);

		};
	}

	@Bean
	CommandLineRunner callWithFeignClient(ReservationClient rc) {

		return args -> {
			rc.getReservations().getContent().forEach(r -> {
				System.out.println(r.getResrvationCount());
			});
		};
	}
}

@FeignClient("eureka.reservation-service")
interface ReservationClient {

	@RequestMapping(path = "/reservations")
	org.springframework.hateoas.Resource<List<Reservation>> getReservations();
}

class Reservation extends ResourceSupport{

	private int resrvationCount;

	private String reservetionFor;

	public Reservation() {
		// JPA Expects default constructor to be available
		super();
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
		builder.append("Reservation [resrvationCount=").append(resrvationCount).append(", reservetionFor=")
				.append(reservetionFor).append("]");
		return builder.toString();
	}

}
