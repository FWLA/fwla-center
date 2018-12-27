package de.ihrigb.fwla.fwlacenter.persistence.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "ambulance_patterns")
public class AmbulancePattern extends BasePattern {
}
