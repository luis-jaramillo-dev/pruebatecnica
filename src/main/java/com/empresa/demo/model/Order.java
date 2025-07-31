package com.empresa.demo.model;

import java.math.BigDecimal;

public class Order {
    private BigDecimal total;
    private boolean vip;

    public Order() { }

    public Order(BigDecimal total, boolean vip) {
        this.total = total;
        this.vip = vip;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public boolean isVip() {
        return vip;
    }

    public void setVip(boolean vip) {
        this.vip = vip;
    }
}
