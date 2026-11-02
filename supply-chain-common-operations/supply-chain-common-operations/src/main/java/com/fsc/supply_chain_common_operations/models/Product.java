package com.fsc.supply_chain_common_operations.models;

import com.fsc.supply_chain_common_operations.enums.ProductStatus;
import com.fsc.supply_chain_common_operations.enums.ProductType;
import com.fsc.supply_chain_common_operations.enums.UnitOfMeasure;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product extends GlobalRecord {

    @Column(name = "product_code", nullable = false, length = 50)
    private String productCode;

    @Column(name = "product_name", nullable = false, length = 200)
    private String productName;

    @Column(length = 2000)
    private String description;

    @ManyToOne
    private ProductCategory category;

    @Column(length = 100)
    private String sku;

    @Column(length = 100)
    private String barcode;

    private String brand;

    private String manufacturer;

    private String modelNumber;

    @ManyToOne
    private SupplierCompany supplierCompany;

    @Enumerated(EnumType.STRING)
    @Column(length = 30, nullable = false)
    private UnitOfMeasure unitOfMeasure;

    @Enumerated(EnumType.STRING)
    @Column(length = 30, nullable = false)
    private ProductType productType = ProductType.PHYSICAL;

    @Enumerated(EnumType.STRING)
    @Column(length = 30, nullable = false)
    private ProductStatus status = ProductStatus.DRAFT;

    @Column(precision = 12, scale = 3)
    private BigDecimal weight;

    private String weightUnit;

    @Column(precision = 12, scale = 3)
    private BigDecimal length;

    @Column(precision = 12, scale = 3)
    private BigDecimal width;

    @Column(precision = 12, scale = 3)
    private BigDecimal height;

    private String dimensionUnit;

    private String imageUrl;
}
