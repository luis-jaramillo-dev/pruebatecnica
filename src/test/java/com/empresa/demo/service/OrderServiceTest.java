package com.empresa.demo.service;

import com.empresa.demo.model.Order;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;


class OrderServiceTest {


    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService();
    }

    @Test
    void testCalcularDescuento_Total500_NoDescuento() {
        // Given
        Order order = new Order(new BigDecimal("500"), false);

        // When
        BigDecimal descuento = orderService.calcularDescuento(order);

        // Then
        assertEquals(new BigDecimal("0"), descuento);
        assertEquals(
                0, descuento.compareTo(BigDecimal.ZERO), "Descuento debe ser 0 para totales menores o iguales a 1000");
    }

    @Test
    void testCalcularDescuento_Total1500_NoVip_Descuento150() {
        // Given
        Order order = new Order(new BigDecimal("1500"), false);

        // When
        BigDecimal descuento = orderService.calcularDescuento(order);

        // Then
        assertEquals(new BigDecimal("150.00"), descuento.setScale(2));
        assertEquals(
                0, descuento.compareTo(new BigDecimal("150")), "Descuento debe ser 150 (10% de 1500)");
    }

    @Test
    void testCalcularDescuento_Total900_Vip_Descuento45() {
        // Given
        Order order = new Order(new BigDecimal("900"), true);

        // When
        BigDecimal descuento = orderService.calcularDescuento(order);

        // Then
        assertEquals(new BigDecimal("45.00"), descuento.setScale(2));
        assertEquals(0, descuento.compareTo(new BigDecimal("45")), "Descuento debe ser 150 (10% de 1500)");
    }

//    @Test
//    void testCalcularDescuento_Total3000_Vip_DescuentoMaximo600() {
//        // Given
//        Order order = new Order(new BigDecimal("3000"), true);
//
//        // When
//        BigDecimal descuento = orderService.calcularDescuento(order);
//
//        // Then
//        assertEquals(new BigDecimal("600.00"), descuento.setScale(2));
//        assertEquals(0, descuento.compareTo(new BigDecimal("600")), "Descuento debe estar limitado a 600 (20% máximo de 3000)");
//    }

    @Test
    void testCalcularDescuento_Total1500_Vip_DescuentoAcumulado() {
        // Given
        Order order = new Order(new BigDecimal("1500"), true);

        // When
        BigDecimal descuento = orderService.calcularDescuento(order);

        // Then
        // Cálculo: 10% de 1500 = 150, luego 5% de (1500-150) = 5% de 1350 = 67.5
        // Total descuento = 150 + 67.5 = 217.5
        assertEquals(new BigDecimal("217.50"), descuento.setScale(2));
        assertEquals(
                0, descuento.compareTo(new BigDecimal("217.5")), "Descuento debe ser 217.5 (10% + 5% VIP sobre monto restante)");
    }

    @Test
    void testCalcularDescuento_BorderCase_Total1000_NoDescuento() {
        // Given
        Order order = new Order(new BigDecimal("1000"), false);

        // When
        BigDecimal descuento = orderService.calcularDescuento(order);

        // Then
        assertEquals(new BigDecimal("0"), descuento);
        assertEquals(0, descuento.compareTo(BigDecimal.ZERO), "No debe haber descuento para total exactamente 1000");
    }

    @Test
    void testCalcularDescuento_BorderCase_Total1001_Descuento100_10() {
        // Given
        Order order = new Order(new BigDecimal("1001"), false);

        // When
        BigDecimal descuento = orderService.calcularDescuento(order);

        // Then
        assertEquals(new BigDecimal("100.10"), descuento.setScale(2));
        assertEquals(
                0, descuento.compareTo(new BigDecimal("100.1")), "Descuento debe ser 100.10 (10% de 1001)");
    }

}