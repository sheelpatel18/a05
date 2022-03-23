package com.comp301.a05driver;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ProximityIterator {
    Iterator<Driver> driverPool;
    Position clientPosition;
    int proximityRange;

    public ProximityIterator(Iterable<Driver> driverPool, Position clientPosition, int proximityRange) {
        this.driverPool = driverPool.iterator();
        this.clientPosition = clientPosition;
        this.proximityRange = proximityRange;
    }
    
    public boolean hasNext() {
        return driverPool.hasNext();
    }

    public Driver next() {
        Driver driver = driverPool.next();
        if (driver.getVehicle().getPosition().getManhattanDistanceTo(clientPosition) <= proximityRange) {
            return driver;
        }
        return next();
        // already throws NoSuchElementException in
    }
    
}