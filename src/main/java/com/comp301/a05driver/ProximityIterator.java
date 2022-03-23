package com.comp301.a05driver;

import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ProximityIterator implements Iterator<Driver> {
    Iterator<Driver> driverPool;
    Position clientPosition;
    int proximityRange;
    private Driver nextDriver;

    public ProximityIterator(Iterable<Driver> driverPool, Position clientPosition, int proximityRange) {
        if (!(driverPool != null && clientPosition != null && proximityRange > 0)) {
            throw new IllegalArgumentException("Invalid arguments");
        }
        this.driverPool = driverPool.iterator();
        this.clientPosition = clientPosition;
        this.proximityRange = proximityRange;
        this.nextDriver = null;
    }
    
    public boolean hasNext() {
        if (nextDriver != null) {
            return true;
        }
        loadNext();
        return nextDriver != null;
    }

    private void loadNext() {
        Driver next = this.getNext();
        if (next != null) {
            this.nextDriver = next;
        }
    }

    private Driver getNext() {
        try {
            Driver driverPoolNext = driverPool.next();
            if (driverPoolNext.getVehicle().getPosition().getManhattanDistanceTo(clientPosition) <= proximityRange) {
                return driverPoolNext;
            } else {
                return getNext();
            }
        } catch (NoSuchElementException e) {
            return null;
        }
        
    }

    public Driver next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        Driver next = nextDriver;
        nextDriver = null;
        return next;
    }
    
}