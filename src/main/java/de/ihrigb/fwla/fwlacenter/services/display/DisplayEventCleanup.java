package de.ihrigb.fwla.fwlacenter.services.display;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import de.ihrigb.fwla.fwlacenter.persistence.repository.DisplayEventRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DisplayEventCleanup {

	private final DisplayEventRepository displayEventRepository;

	@Transactional
	@Scheduled(fixedRate = 60000)
	public void deleteOutdatedDisplayEvents() {
		displayEventRepository.deleteOutdatedDisplayEvents();
	}
}
