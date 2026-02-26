package com.tutorial.tutorial;

import com.tutorial.tutorial.models.ERole;
import com.tutorial.tutorial.models.Role;
import com.tutorial.tutorial.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TutorialApplication {

	public static void main(String[] args) {
		SpringApplication.run(TutorialApplication.class, args);
	}

	@Bean
	CommandLineRunner seedRoles(RoleRepository roleRepository) {
		return args -> {
			if (!roleRepository.findByName(ERole.ROLE_USER).isPresent()) {
				roleRepository.save(new Role(ERole.ROLE_USER));
			}
			if (!roleRepository.findByName(ERole.ROLE_MODERATOR).isPresent()) {
				roleRepository.save(new Role(ERole.ROLE_MODERATOR));
			}
			if (!roleRepository.findByName(ERole.ROLE_ADMIN).isPresent()) {
				roleRepository.save(new Role(ERole.ROLE_ADMIN));
			}
		};
	}

}
