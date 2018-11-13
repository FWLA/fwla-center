package de.ihrigb.fwla.fwlacenter.persistence.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "active_resources")
public class ActiveResource extends Resource {
}
