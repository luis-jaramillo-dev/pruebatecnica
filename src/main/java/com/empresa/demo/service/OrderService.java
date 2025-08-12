package com.empresa.demo.service;

import java.math.BigDecimal;

import com.empresa.demo.model.Order;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    public BigDecimal calcularDescuento(Order order) {

        BigDecimal total = order.getTotal();
        BigDecimal descuentoAcumulado = BigDecimal.ZERO;
        BigDecimal descuentoMaximo = total.multiply(new BigDecimal("0.20"));

        if (total.compareTo(new BigDecimal("1000")) > 0) {
            BigDecimal descuento10 = total.multiply(new BigDecimal("0.10"));
            descuentoAcumulado = descuentoAcumulado.add(descuento10);
        }

        if (order.isVip()) {
            BigDecimal totalDespuesPrimerDescuento = total.subtract(descuentoAcumulado);
            BigDecimal descuentoVip = totalDespuesPrimerDescuento.multiply(new BigDecimal("0.05"));
            descuentoAcumulado = descuentoAcumulado.add(descuentoVip);
        }

        if (descuentoAcumulado.compareTo(descuentoMaximo) > 0) {
            descuentoAcumulado = descuentoMaximo;
        }

        return descuentoAcumulado;
    }
}
