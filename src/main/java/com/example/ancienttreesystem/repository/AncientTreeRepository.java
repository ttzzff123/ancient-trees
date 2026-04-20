package com.example.ancienttreesystem.repository;

import com.example.ancienttreesystem.entity.AncientTree;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AncientTreeRepository extends JpaRepository<AncientTree, Long> {

    // ==================== 自动生成的基本方法（不用写代码就有） ====================
    // save() - 保存/更新
    // findById() - 根据ID查询
    // findAll() - 查询所有
    // deleteById() - 根据ID删除
    // count() - 统计数量
    // existsById() - 判断是否存在

    // ==================== 自定义查询方法（按规则命名，自动实现） ====================

    /**
     * 根据树种名称查询
     * 方法名规则：findBy + 属性名
     */
    List<AncientTree> findBySpecies(String species);

    /**
     * 根据保护等级查询
     */
    List<AncientTree> findByProtectionLevel(String protectionLevel);

    /**
     * 根据生长状况查询
     */
    List<AncientTree> findByGrowthStatus(String growthStatus);

    /**
     * 根据所在乡镇查询
     */
    List<AncientTree> findByTown(String town);

    /**
     * 根据权属查询（国有/集体）
     */
    List<AncientTree> findByOwnership(String ownership);

    /**
     * 模糊查询树种名称（包含关键字）
     * 方法名规则：findBy + 属性名 + Containing
     */
    List<AncientTree> findBySpeciesContaining(String keyword);

    /**
     * 查询树龄大于指定年份的古树
     * 方法名规则：findBy + 属性名 + GreaterThan
     */
    List<AncientTree> findByTreeAgeGreaterThan(Integer age);

    /**
     * 查询树龄在指定范围内的古树
     * 方法名规则：findBy + 属性名 + Between
     */
    List<AncientTree> findByTreeAgeBetween(Integer minAge, Integer maxAge);

    /**
     * 根据林区查询
     */
    List<AncientTree> findByForestArea(String forestArea);

    /**
     * 根据乡镇和树种联合查询
     * 方法名规则：findBy + 属性1 + And + 属性2
     */
    List<AncientTree> findByTownAndSpecies(String town, String species);

    // ========== 新增：分页查询所有 ==========
    /**
     * 分页查询所有古树
     * Spring Data JPA 自动实现！
     */
    Page<AncientTree> findAll(Pageable pageable);

    // ========== 新增：分页 + 条件查询 ==========
    /**
     * 根据树种分页查询
     */
    Page<AncientTree> findBySpeciesContaining(String species, Pageable pageable);

    /**
     * 根据保护等级分页查询
     */
    Page<AncientTree> findByProtectionLevel(String protectionLevel, Pageable pageable);

    // ========== 新增：高级搜索（动态SQL）==========

    /**
     * 高级搜索 - 动态条件查询
     * 所有参数都可为空，为空时不参与过滤
     */
    @Query("""
        SELECT t FROM AncientTree t 
        WHERE (:species IS NULL OR t.species LIKE CONCAT('%', :species, '%'))
          AND (:level IS NULL OR t.protectionLevel = :level)
          AND (:town IS NULL OR t.town LIKE CONCAT('%', :town, '%'))
          AND (:forestArea IS NULL OR t.forestArea LIKE CONCAT('%', :forestArea, '%'))
          AND (:status IS NULL OR t.growthStatus = :status)
          AND (:ownership IS NULL OR t.ownership = :ownership)
          AND (:minAge IS NULL OR t.treeAge >= :minAge)
          AND (:maxAge IS NULL OR t.treeAge <= :maxAge)
        """)
    Page<AncientTree> advancedSearch(
            @Param("species") String species,
            @Param("level") String level,
            @Param("town") String town,
            @Param("forestArea") String forestArea,
            @Param("status") String status,
            @Param("ownership") String ownership,
            @Param("minAge") Integer minAge,
            @Param("maxAge") Integer maxAge,
            Pageable pageable);
}
