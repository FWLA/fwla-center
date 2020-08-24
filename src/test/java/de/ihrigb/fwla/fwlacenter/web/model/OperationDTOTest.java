package de.ihrigb.fwla.fwlacenter.web.model;

import java.util.Collections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.ihrigb.fwla.fwlacenter.persistence.model.Operation;

public class OperationDTOTest {

    @Test
    public void testNullInResourceList() throws Exception {
        Operation operation = new Operation();
        operation.setResources(Collections.singletonList(null));

        Assertions.assertTrue(new OperationDTO(operation).getResources().isEmpty());
    }
}