package de.ihrigb.fwla.fwlacenter.persistence.repository;

import java.util.Set;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import de.ihrigb.fwla.fwlacenter.persistence.model.Operation;

public interface OperationRepository extends JpaRepository<Operation, String> {

	@Query("select o from Operation o")
	Stream<Operation> streamAll();

	Stream<Operation> findByYear(int year);

	@Query("select o.year from Operation o group by o.year")
	Set<Integer> findYears();

	@Query("select o from Operation o where o.closed = false")
	Stream<Operation> streamByClosedFalse();
}
