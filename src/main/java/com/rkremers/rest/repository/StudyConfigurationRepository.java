package com.rkremers.rest.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.rkremers.rest.model.StudyConfiguration;

public interface StudyConfigurationRepository  extends JpaRepository<StudyConfiguration, Long> {

	public Optional<StudyConfiguration> findByParameterName(String parameterName);
	
	public String findParameterValue(@Param("parameterName") String parameterName);
}
