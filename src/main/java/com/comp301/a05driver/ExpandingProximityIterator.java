package com.comp301.a05driver;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ExpandingProximityIterator implements Iterator<Driver> {
    Iterable<Driver> driverPool;
    Iterator<Driver> driverPoolIterator;
    Position clientPosition;
    int expansionStep;
    int currentStep;
    Driver nextDriver;

    public ExpandingProximityIterator(Iterable<Driver> driverPool, Position clientPosition, int expansionStep) {
        if (!(driverPool != null && clientPosition != null && expansionStep > 0)) {
            throw new IllegalArgumentException("Invalid arguments");
        }
        this.driverPool = driverPool;
        this.driverPoolIterator = driverPool.iterator();
        this.clientPosition = clientPosition;
        this.expansionStep = expansionStep;
        this.currentStep = 0;
        this.nextDriver = null;
    }

    @Override
    public boolean hasNext() {
        if (nextDriver != null) {
            return true;
        }
        loadNext();
        if (nextDriver == null) { 
            iterate();
            reset();
            loadNext();
        }
        return nextDriver != null;
    }

    private void iterate() {
        this.currentStep++;
    }

    private void loadNext() {
        Driver next = this.getNext();
        if (next != null) {
            this.nextDriver = next;
        }
    }

    private void reset() {
        this.driverPoolIterator = driverPool.iterator();
    }

    private Driver getNext() {
        try {
            Driver driverPoolNext = driverPoolIterator.next();
            if (driverPoolNext.getVehicle().getPosition().getManhattanDistanceTo(clientPosition) <= 1 + currentStep * expansionStep && driverPoolNext.getVehicle().getPosition().getManhattanDistanceTo(clientPosition) >= 1 + expansionStep) {
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
