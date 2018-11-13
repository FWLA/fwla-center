package de.ihrigb.fwla.fwlacenter.persistence.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "passive_resources")
public class PassiveResource extends Resource {

	@ManyToMany
	@JoinTable(name = "passive_active_resources", joinColumns = @JoinColumn(name = "passive_resource_id", nullable = false), inverseJoinColumns = @JoinColumn(name = "active_resource_id", nullable = false))
	private Set<ActiveResource> attachableTo;
}
