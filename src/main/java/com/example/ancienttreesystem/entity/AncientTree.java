package com.example.ancienttreesystem.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 古树名木实体类
 * 对应数据库表：ancient_trees
 */
@Data
@Entity
@Table(name = "ancient_trees")
public class AncientTree {

    // ==================== 主键ID ====================
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ==================== 基础信息 ====================
    @Column(name = "object_id")
    private Integer objectId;

    @Column(name = "tree_code", length = 50)
    private String treeCode;

    @Column(name = "survey_order")
    private Integer surveyOrder;

    @Column(name = "tree_group_code", length = 50)
    private String treeGroupCode;

    // ==================== 行政区划 ====================
    @Column(length = 50)
    private String province;

    @Column(length = 50)
    private String city;

    @Column(length = 50)
    private String county;

    @Column(name = "district_code", length = 20)
    private String districtCode;

    @Column(length = 100)
    private String town;

    @Column(length = 100)
    private String village;

    @Column(name = "forest_area", length = 100)
    private String forestArea;

    // ==================== 古树分类 ====================
    @Column(name = "tree_category", length = 50)
    private String treeCategory;

    @Column(name = "census_scope", length = 50)
    private String censusScope;

    @Column(name = "distribution_type", length = 50)
    private String distributionType;

    @Column(name = "change_status", length = 50)
    private String changeStatus;

    @Column(name = "add_reason", length = 100)
    private String addReason;

    @Column(name = "delete_reason", length = 100)
    private String deleteReason;

    // ==================== 植物学信息 ====================
    @Column(length = 100)
    private String family;

    @Column(length = 100)
    private String genus;

    @Column(length = 100)
    private String species;

    @Column(name = "latin_name", length = 200)
    private String latinName;

    // ==================== 树龄与保护 ====================
    @Column(name = "tree_age")
    private Integer treeAge;

    @Column(name = "age_measurement_method", length = 100)
    private String ageMeasurementMethod;

    @Column(name = "protection_level", length = 20)
    private String protectionLevel;

    // ==================== 生长状况 ====================
    @Column(name = "growth_status", length = 50)
    private String growthStatus;

    @Column(precision = 5, scale = 2)
    private BigDecimal height;

    @Column(precision = 6, scale = 2)
    private BigDecimal dbh;

    @Column(name = "ground_diameter", precision = 6, scale = 2)
    private BigDecimal groundDiameter;

    @Column(name = "crown_spread", precision = 5, scale = 2)
    private BigDecimal crownSpread;

    @Column(precision = 6, scale = 2)
    private BigDecimal circumference;

    @Column(name = "ground_circumference", precision = 6, scale = 2)
    private BigDecimal groundCircumference;

    // ==================== 位置信息 ====================
    @Column(name = "location_type", length = 100)
    private String locationType;

    @Column(precision = 12, scale = 8)
    private BigDecimal longitude;

    @Column(precision = 12, scale = 8)
    private BigDecimal latitude;

    @Column(precision = 8, scale = 2)
    private BigDecimal altitude;

    @Column(name = "slope_position", length = 20)
    private String slopePosition;

    @Column(name = "slope_gradient", length = 20)
    private String slopeGradient;

    @Column(name = "slope_aspect", length = 20)
    private String slopeAspect;

    // ==================== 土壤环境 ====================
    @Column(name = "soil_compaction", length = 20)
    private String soilCompaction;

    @Column(name = "soil_depth", length = 20)
    private String soilDepth;

    @Column(name = "environment_grade", length = 20)
    private String environmentGrade;

    // ==================== 损伤与干扰 ====================
    @Column(name = "tree_damage", length = 500)
    private String treeDamage;

    @Column(name = "human_disturbance", length = 500)
    private String humanDisturbance;

    // ==================== 管理信息 ====================
    @Column(length = 100)
    private String department;

    @Column(length = 50)
    private String ownership;

    @Column(name = "protection_measures", length = 500)
    private String protectionMeasures;

    @Column(length = 100)
    private String maintainer;

    // ==================== 价值与特征 ====================
    @Column(name = "key_value", length = 500)
    private String keyValue;

    @Column(name = "value_description", columnDefinition = "TEXT")
    private String valueDescription;

    @Column(name = "special_features", length = 500)
    private String specialFeatures;

    @Column(columnDefinition = "TEXT")
    private String remarks;

    // ==================== 调查信息 ====================
    @Column(length = 100)
    private String surveyor;

    @Column(name = "survey_date")
    private LocalDate surveyDate;

    @Column(name = "photo_description", columnDefinition = "TEXT")
    private String photoDescription;

    // ==================== 系统字段 ====================
    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @Column(name = "is_deleted")
    private Integer isDeleted;
}
