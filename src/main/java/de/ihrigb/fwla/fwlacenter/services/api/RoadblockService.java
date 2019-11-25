package de.ihrigb.fwla.fwlacenter.services.api;

import java.util.Set;

import de.ihrigb.fwla.fwlacenter.api.Coordinate;
import de.ihrigb.fwla.fwlacenter.persistence.model.Roadblock;

public interface RoadblockService {

    Set<Roadblock> getWithinBounds(Coordinate upperLeftCoordinate, Coordinate lowerRighCoordinate);
}
