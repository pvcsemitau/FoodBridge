package com.foodbridge.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="donations")
public class Donation {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional=false, fetch=FetchType.LAZY) @JoinColumn(name="restaurant_id") private AppUser restaurant;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="ngo_id") private AppUser ngo;
    @Column(nullable=false, length=140) private String foodName;
    @Column(nullable=false, length=40) private String foodType;
    @Column(nullable=false, precision=10, scale=2) private BigDecimal quantity;
    @Column(nullable=false, length=20) private String unit;
    @Column(nullable=false, length=250) private String pickupLocation;
    @Column(nullable=false) private LocalDateTime preparedAt;
    @Column(nullable=false) private LocalDateTime expiryAt;
    @Enumerated(EnumType.STRING) @Column(nullable=false, length=20) private DonationStatus status=DonationStatus.AVAILABLE;
    @Column(nullable=false) private LocalDateTime createdAt=LocalDateTime.now();

    public Long getId(){return id;} public AppUser getRestaurant(){return restaurant;}
    public void setRestaurant(AppUser v){restaurant=v;} public AppUser getNgo(){return ngo;}
    public void setNgo(AppUser v){ngo=v;} public String getFoodName(){return foodName;}
    public void setFoodName(String v){foodName=v;} public String getFoodType(){return foodType;}
    public void setFoodType(String v){foodType=v;} public BigDecimal getQuantity(){return quantity;}
    public void setQuantity(BigDecimal v){quantity=v;} public String getUnit(){return unit;}
    public void setUnit(String v){unit=v;} public String getPickupLocation(){return pickupLocation;}
    public void setPickupLocation(String v){pickupLocation=v;} public LocalDateTime getPreparedAt(){return preparedAt;}
    public void setPreparedAt(LocalDateTime v){preparedAt=v;} public LocalDateTime getExpiryAt(){return expiryAt;}
    public void setExpiryAt(LocalDateTime v){expiryAt=v;} public DonationStatus getStatus(){return status;}
    public void setStatus(DonationStatus v){status=v;} public LocalDateTime getCreatedAt(){return createdAt;}
}
