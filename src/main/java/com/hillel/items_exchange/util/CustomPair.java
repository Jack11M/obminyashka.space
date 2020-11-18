package com.hillel.items_exchange.util;

public class CustomPair<T, T1> {
    private T t;
    private T1 t1;

    public CustomPair(T t, T1 t1) {
        this.t = t;
        this.t1 = t1;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    public T1 getT1() {
        return t1;
    }

    public void setT1(T1 t1) {
        this.t1 = t1;
    }
}
