package com.rkremers.rest.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.hateoas.ResourceSupport;

/**
 * Purpose:
 * Definition of a table STUDY_CONFIGURATION specific for configuration of the application.
 * Note that this table is intended for application properties and not for system / environment properties.
 * These properties would normally be defined in file application.properties.
 * 
 * @author LTAdmin
 *
 */
@Entity
@Table( name="STUDY_CONFIGURATION"
, uniqueConstraints= @UniqueConstraint(columnNames= {"PARAMETER_NAME", "PARAMETER_STR_VALUE"} ))
@NamedQuery(name="StudyConfiguration.findParameterValue"
			, query="SELECT sc.parameterStrValue FROM StudyConfiguration sc WHERE sc.parameterName = :parameterName"
			)
public class StudyConfiguration extends ResourceSupport implements Serializable {

	private static final long serialVersionUID = -3533545622832386741L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="CONFIGURATION_ID")
	private long configurationId;
	@Column(name="PARAMETER_NAME", nullable=false)
	private String parameterName;
	@Column(name="PARAMETER_STR_VALUE")
	private String parameterStrValue;

	@Column(name="DESCRIPTION", nullable=false)
	private String description;
	
	public StudyConfiguration() {}

	public StudyConfiguration(String parameterName, String parameterStrValue, String description) {
		super();
		this.parameterName = parameterName;
		this.parameterStrValue = parameterStrValue;
		this.description = description;
	}

	public long getConfigurationId() {
		return configurationId;
	}

	public void setConfigurationId(long configurationId) {
		this.configurationId = configurationId;
	}

	public String getParameterName() {
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	public String getParameterStrValue() {
		return parameterStrValue;
	}

	public void setParameterStrValue(String parameterStrValue) {
		this.parameterStrValue = parameterStrValue;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (int) (configurationId ^ (configurationId >>> 32));
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((parameterName == null) ? 0 : parameterName.hashCode());
		result = prime * result + ((parameterStrValue == null) ? 0 : parameterStrValue.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		StudyConfiguration other = (StudyConfiguration) obj;
		if (configurationId != other.configurationId)
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (parameterName == null) {
			if (other.parameterName != null)
				return false;
		} else if (!parameterName.equals(other.parameterName))
			return false;
		if (parameterStrValue == null) {
			if (other.parameterStrValue != null)
				return false;
		} else if (!parameterStrValue.equals(other.parameterStrValue))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "StudyConfiguration [configurationId=" + configurationId + ", parameterName=" + parameterName
				+ ", parameterStrValue=" + parameterStrValue + ", description=" + description + "]";
	}

}
