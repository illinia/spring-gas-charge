package com.portfolio.gascharge;

import com.portfolio.gascharge.domain.member.Address;
import com.portfolio.gascharge.domain.member.Member;
import com.portfolio.gascharge.domain.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class GaschargeApplication {
	public static void main(String[] args) {
		SpringApplication.run(GaschargeApplication.class, args);
	}

//	@Autowired
//	MemberRepository memberRepository;

//	@PostConstruct
//	public void init() {
//		String address1 = "address1";
//		String address2 = "address2";
//		String zipcode = "12345";
//		Address address = new Address(address1, address2, zipcode);
//
//
//		String name = "test";
//		Member member = new Member(name, address);
//
//		memberRepository.save(member);
//	}
}
