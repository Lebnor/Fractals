package com.example.ccurvefractal;

public class Drawer {

    enum Fractal {
        CCURVE(16), TRIANGLE(8);
        private final int maxOrder;

        Fractal(int maxOrder) {
            this.maxOrder = maxOrder;
        }
    }

    /**
     * Increments order and returns true if max order is reached
     * @return true if current order is max order
     */
    public boolean increaseOrder() {
        this.order++;
        return order == maxOrder;
    }

    /**
     * Reduces order and returns true if min order reached
     * @return true if order = 1
     */
    public boolean reduceOrder() {
        this.order--;
        return order == 1;
    }

    private int order;
    private int maxOrder;
    private Fractal fractal;

    public Drawer(Fractal fractal) {
        this.order = 0;
        this.maxOrder = fractal.maxOrder;
        this.fractal = fractal;
    }


    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getMaxOrder() {
        return maxOrder;
    }

    public void setMaxOrder(int maxOrder) {
        this.maxOrder = maxOrder;
    }

    public Fractal getFractal() {
        return fractal;
    }

    public void setFractal(Fractal fractal) {
        this.maxOrder = fractal.maxOrder;
        this.fractal = fractal;
    }
}
