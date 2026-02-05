/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.compactcode.javadb.model;

/**
 *
 * @author Zhgun N.A.
 */

// Модель продукта с полями id, name, description, price, stock
// Два конструктора: без ID (для вставки с автоинкрементом) и с ID (для выборки/обновления)
public class Product {

    private int id;
    private String name;
    private String description;
    private double price;
    private int stock;

    
    public Product(String name, String description, double price, int stock) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
    }

    
    public Product(int id, String name, String description, double price, int stock) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }
}
