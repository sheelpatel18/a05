package com.comp301.a05driver;

import java.util.Iterator;
import java.util.*;

enum Direction {
    FORWARD, BACKWARD
}

public class SnakeOrderAcrossPoolsIterator implements Iterator<Driver> {
    private List<Iterator<Driver>> driverPools;
    private Driver nextDriver;
    private int size;
    private Integer currentIndex = null;
    private Direction direction = Direction.FORWARD;

    
    public SnakeOrderAcrossPoolsIterator(List<Iterable<Driver>> driverPools) {
        if (!(driverPools != null && driverPools.size() > 0)) {
            //throw new IllegalArgumentException("Invalid arguments");
        }
        this.driverPools = new ArrayList<Iterator<Driver>>();
        for (Iterable<Driver> driverPool : driverPools) {
            this.driverPools.add(driverPool.iterator());
        }
        this.size = driverPools.size();
    }

    private void iterateIndex() {
        if (this.currentIndex == null) {this.currentIndex = 0; return;}
        if (this.currentIndex == this.size && direction == Direction.FORWARD) { direction = Direction.BACKWARD; }
        if (this.currentIndex == 0 && direction == Direction.BACKWARD) { direction = Direction.FORWARD; }
        currentIndex += direction == Direction.FORWARD ? 1 : -1;
    }

    private void loadNext(int iterationCount) {
        if (iterationCount == this.size) {
            this.nextDriver = null;
            return;
        }
        iterateIndex();
        if (this.currentIndex == null) {
            return;
        }
        Driver driver = getNext();
        if (driver == null) {
            loadNext(iterationCount + 1);
        }
    }

    private Driver getNext() {
        try {
            Driver driverPoolNext = driverPools.get(this.currentIndex).next();
            return driverPoolNext;
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    @Override
    public boolean hasNext() {
        if (nextDriver != null) {
            return true;
        } else {
            loadNext(1);
            return nextDriver != null;
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
