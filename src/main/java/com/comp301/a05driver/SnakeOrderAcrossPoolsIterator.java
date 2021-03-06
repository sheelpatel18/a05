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
    private Integer currentIndex = -1;
    private Direction direction = Direction.FORWARD;
    private boolean repeat = false;
    private boolean start = true;


    public SnakeOrderAcrossPoolsIterator(List<Iterable<Driver>> driverPools) {
        if (!(driverPools != null && driverPools.size() > 0)) {
            //throw new IllegalArgumentException("Invalid arguments");
        }
        this.driverPools = new ArrayList<Iterator<Driver>>();
        for (Iterable<Driver> driverPool : driverPools) {
            this.driverPools.add(driverPool.iterator());
        }
        this.size = this.driverPools.size();
    }

    private void iterateIndex() {
        if (currentIndex == 0 && currentIndex == this.size -1) {return;}
        if (this.currentIndex == 0) {
            this.direction = Direction.FORWARD;
        } else if (this.currentIndex == this.size - 1) {
            this.direction = Direction.BACKWARD;
        }
        if (this.repeat) { this.repeat = false; return;}
        currentIndex += this.direction == Direction.FORWARD ? 1 : -1;
        if ((this.currentIndex == 0 || this.currentIndex == this.size - 1) && !this.start) {
            this.repeat = true;
        }
        this.start = false;
    }

    private void loadNext() {loadNext(0);}

    private void loadNext(int iterationCount) {
        if (iterationCount == this.size*4) {
            this.nextDriver = null;
            return;
        }
        iterateIndex();
        if (this.currentIndex == null || this.currentIndex < 0 || this.currentIndex >= this.size) {
            return;
        }
        Driver driver = getNext();
        if (driver == null) {
            loadNext(iterationCount + 1);
        } else {
            this.nextDriver = driver;
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
        if (this.size == 0) {return false;}
        if (nextDriver != null) {
            return true;
        } else {
            loadNext();
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
