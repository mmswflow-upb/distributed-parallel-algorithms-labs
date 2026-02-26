package com.tutorial.tutorial;

import com.tutorial.tutorial.models.ERole;
import com.tutorial.tutorial.models.Role;
import com.tutorial.tutorial.models.User;
import com.tutorial.tutorial.repository.RoleRepository;
import com.tutorial.tutorial.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class TutorialApplication {

	public static void main(String[] args) {
		SpringApplication.run(TutorialApplication.class, args);
	}

	@Bean
	CommandLineRunner seedRolesAndUsers(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseGet(() -> roleRepository.save(new Role(ERole.ROLE_USER)));
			Role moderatorRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
					.orElseGet(() -> roleRepository.save(new Role(ERole.ROLE_MODERATOR)));
			Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
					.orElseGet(() -> roleRepository.save(new Role(ERole.ROLE_ADMIN)));

			if (!userRepository.existsByUsername("moderator")) {
				User moderator = new User(
						"moderator",
						"moderator@lab2.local",
						passwordEncoder.encode("mod12345")
				);
				Set<Role> roles = new HashSet<>();
				roles.add(userRole);
				roles.add(moderatorRole);
				moderator.setRoles(roles);
				userRepository.save(moderator);
			}

			if (!userRepository.existsByUsername("admin")) {
				User admin = new User(
						"admin",
						"admin@lab2.local",
						passwordEncoder.encode("admin12345")
				);
				Set<Role> roles = new HashSet<>();
				roles.add(userRole);
				roles.add(adminRole);
				admin.setRoles(roles);
				userRepository.save(admin);
			}
		};
	}

}
