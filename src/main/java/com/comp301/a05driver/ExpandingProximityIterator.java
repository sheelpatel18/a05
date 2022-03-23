package com.comp301.a05driver;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ExpandingProximityIterator implements Iterator<Driver> {
    Iterable<Driver> driverPool;
    Iterator<Driver> driverPoolIterator;
    Position clientPosition;
    int expansionStep;
    int lowerBound;
    int upperBound;
    Driver nextDriver;

    public ExpandingProximityIterator(Iterable<Driver> driverPool, Position clientPosition, int expansionStep) {
        if (!(driverPool != null && clientPosition != null && expansionStep > 0)) {
            throw new IllegalArgumentException("Invalid arguments");
        }
        this.driverPool = driverPool;
        this.driverPoolIterator = driverPool.iterator();
        this.clientPosition = clientPosition;
        this.expansionStep = expansionStep;
        this.nextDriver = null;
    }

    @Override
    public boolean hasNext() {
        if (nextDriver != null) {
            return true;
        }
        loadNext();
        return nextDriver != null;
    }

    private void iterate() {
        this.lowerBound = this.upperBound;
        this.upperBound = this.lowerBound + this.expansionStep;
    }

    private void loadNext() {
        Driver next = this.getNext();
        if (next != null) {
            this.nextDriver = next;
        } else {
            reset();
            iterate();
            loadNext();
        }
    }

    private void reset() {
        this.driverPoolIterator = driverPool.iterator();
    }

    private Driver getNext() {
        try {
            Driver driverPoolNext = driverPoolIterator.next();
            if (driverPoolNext.getVehicle().getPosition().getManhattanDistanceTo(clientPosition) <= upperBound && driverPoolNext.getVehicle().getPosition().getManhattanDistanceTo(clientPosition) > lowerBound) {
                return driverPoolNext;
            } else {
                return getNext();
            }
        } catch (NoSuchElementException e) {
            return null;
        }

    }

    @Override
    public Driver next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        Driver next = nextDriver;
        nextDriver = null;
        return next;
    }

}
