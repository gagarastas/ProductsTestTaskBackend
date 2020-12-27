package com.gagara.products.entities;

import javax.persistence.*;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private Long calories;

    @Column(name = "imagePath")
    private String imagePath;

    public Product(){
    }
    public Product(String name, Long calories, String imagePath) {
        this.name = name;
        this.calories = calories;
        this.imagePath = imagePath;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getCalories() {
        return calories;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCalories(Long calories) {
        this.calories = calories;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
