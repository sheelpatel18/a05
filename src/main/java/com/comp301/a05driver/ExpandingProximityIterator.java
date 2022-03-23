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
    int size;
    boolean exhausted = false;

    public ExpandingProximityIterator(Iterable<Driver> driverPool, Position clientPosition, int expansionStep) {
        if (!(driverPool != null && clientPosition != null && expansionStep > 0)) {
            throw new IllegalArgumentException("Invalid arguments");
        }
        this.driverPool = driverPool;
        this.driverPoolIterator = driverPool.iterator();
        this.clientPosition = clientPosition;
        this.expansionStep = expansionStep;
        this.lowerBound = 0;
        this.upperBound = 1;
        this.nextDriver = null;
        getSize();
    }

    @Override
    public boolean hasNext() {
        if (nextDriver != null) {
            return true;
        }
        loadNext();
        return nextDriver != null;
    }

    private void getSize() {
        try {
            while (true) {
                if (driverPoolIterator.next() != null) {
                    this.size++;
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            return;
        } finally {
            reset();
        }
    }

    private void iterate() {
        this.lowerBound = this.upperBound;
        this.upperBound = this.lowerBound + this.expansionStep;
    }


    private void loadNext() {
        if (this.exhausted) {return;}
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

    private Driver getNext() {return getNext(0);}

    private Driver getNext(int iterations) {
        try {
            if (iterations == this.size) {this.exhausted = true; return null;}
            Driver driverPoolNext = driverPoolIterator.next();
            if (driverPoolNext.getVehicle().getPosition().getManhattanDistanceTo(clientPosition) <= upperBound && (driverPoolNext.getVehicle().getPosition().getManhattanDistanceTo(clientPosition) > lowerBound || driverPoolNext.getVehicle().getPosition().getManhattanDistanceTo(clientPosition) == 0)) {
                return driverPoolNext;
            } else {
                return getNext(iterations + 1);
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
