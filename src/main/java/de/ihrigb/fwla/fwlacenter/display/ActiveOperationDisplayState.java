package de.ihrigb.fwla.fwlacenter.display;

import de.ihrigb.fwla.fwlacenter.services.api.Operation;
import lombok.Getter;

@Getter
public class ActiveOperationDisplayState extends BaseDisplayState {
	private final Operation operation;

	public ActiveOperationDisplayState(Operation operation) {
		super("operation");
		this.operation = operation;
	}
}
