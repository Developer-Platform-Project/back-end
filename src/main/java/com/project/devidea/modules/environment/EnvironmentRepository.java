package com.project.devidea.modules.environment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EnvironmentRepository extends JpaRepository<Environment, Long> {

    Environment findByDescription(String description);
}
