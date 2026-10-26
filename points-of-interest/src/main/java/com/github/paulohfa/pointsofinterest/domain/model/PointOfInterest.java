package com.github.paulohfa.pointsofinterest.domain.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_points_of_interest")
public class PointOfInterest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "poi_name", nullable = false, length = 100)
    private String name;

    @Column(name = "coord_x", nullable = false)
    private Integer x;

    @Column(name = "coord_y", nullable = false)
    private Integer y;

    public PointOfInterest() {
    }

    public PointOfInterest(String name, Integer x, Integer y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public PointOfInterest(Long id, String name, Integer x, Integer y) {
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }
}
