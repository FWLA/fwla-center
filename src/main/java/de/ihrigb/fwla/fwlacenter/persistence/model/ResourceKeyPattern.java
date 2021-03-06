package de.ihrigb.fwla.fwlacenter.persistence.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "resourcekey_patterns")
public class ResourceKeyPattern extends BasePattern {
}
