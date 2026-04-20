package com.example.ancienttreesystem.service;

import com.example.ancienttreesystem.entity.AncientTree;
import com.example.ancienttreesystem.repository.AncientTreeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AncientTreeService {
    // 自动注入 Repository（Lombok 的 @RequiredArgsConstructor 帮我们生成构造函数）
    private final AncientTreeRepository ancientTreeRepository;

    // ==================== 增删改查基础方法 ====================

    /**
     * 查询所有古树
     */
    public List<AncientTree> findAll() {
        return ancientTreeRepository.findAll();
    }

    /**
     * 根据ID查询古树
     */
    public Optional<AncientTree> findById(Long id) {
        return ancientTreeRepository.findById(id);
    }

    /**
     * 保存古树（新增或更新）
     */
    public AncientTree save(AncientTree tree) {
        return ancientTreeRepository.save(tree);
    }

    /**
     * 删除古树
     */
    public void deleteById(Long id) {
        ancientTreeRepository.deleteById(id);
    }

    // ==================== 业务查询方法 ====================

    /**
     * 根据树种查询
     */
    public List<AncientTree> findBySpecies(String species) {
        return ancientTreeRepository.findBySpecies(species);
    }

    /**
     * 根据保护等级查询
     */
    public List<AncientTree> findByProtectionLevel(String level) {
        return ancientTreeRepository.findByProtectionLevel(level);
    }

    /**
     * 根据乡镇查询
     */
    public List<AncientTree> findByTown(String town) {
        return ancientTreeRepository.findByTown(town);
    }

    /**
     * 模糊查询树种名称
     */
    public List<AncientTree> searchBySpeciesKeyword(String keyword) {
        return ancientTreeRepository.findBySpeciesContaining(keyword);
    }

    /**
     * 查询指定树龄以上的古树
     */
    public List<AncientTree> findOldTrees(Integer minAge) {
        return ancientTreeRepository.findByTreeAgeGreaterThan(minAge);
    }

    /**
     * 查询树龄范围内的古树
     */
    public List<AncientTree> findByAgeRange(Integer minAge, Integer maxAge) {
        return ancientTreeRepository.findByTreeAgeBetween(minAge, maxAge);
    }

    /**
     * 根据林区查询
     */
    public List<AncientTree> findByForestArea(String forestArea) {
        return ancientTreeRepository.findByForestArea(forestArea);
    }

    /**
     * 根据乡镇和树种联合查询
     */
    public List<AncientTree> findByTownAndSpecies(String town, String species) {
        return ancientTreeRepository.findByTownAndSpecies(town, species);
    }

    /**
     * 统计古树总数
     */
    public long count() {
        return ancientTreeRepository.count();
    }

    // ========== 新增：分页查询方法 ==========

    /**
     * 分页查询所有古树
     * @param page 当前页码（从0开始）
     * @param size 每页条数
     * @return 分页结果
     */
    public Page<AncientTree> findAllByPage(int page, int size) {
        // 按ID倒序排序（新的在前）
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return ancientTreeRepository.findAll(pageable);
    }

    /**
     * 分页 + 模糊查询树种
     */
    public Page<AncientTree> searchBySpeciesPage(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return ancientTreeRepository.findBySpeciesContaining(keyword, pageable);
    }

    /**
     * 分页 + 按保护等级查询
     */
    public Page<AncientTree> findByProtectionLevelPage(String level, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return ancientTreeRepository.findByProtectionLevel(level, pageable);
    }

    // ========== 新增：高级搜索 ==========

    /**
     * 高级搜索 - 多条件组合查询
     *
     * @param species 树种（模糊）
     * @param level 保护等级
     * @param town 乡镇（模糊）
     * @param forestArea 林区（模糊）
     * @param status 生长状况
     * @param ownership 权属
     * @param minAge 最小树龄
     * @param maxAge 最大树龄
     * @param page 页码
     * @param size 每页条数
     * @return 分页结果
     */
    public Page<AncientTree> advancedSearch(
            String species,
            String level,
            String town,
            String forestArea,
            String status,
            String ownership,
            Integer minAge,
            Integer maxAge,
            int page,
            int size) {

        // 处理空字符串：前端传空字符串时，转为null
        String finalSpecies = StringUtils.hasText(species) ? species : null;
        String finalLevel = StringUtils.hasText(level) ? level : null;
        String finalTown = StringUtils.hasText(town) ? town : null;
        String finalForestArea = StringUtils.hasText(forestArea) ? forestArea : null;
        String finalStatus = StringUtils.hasText(status) ? status : null;
        String finalOwnership = StringUtils.hasText(ownership) ? ownership : null;

        // 分页参数
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        // 调用Repository
        return ancientTreeRepository.advancedSearch(
                finalSpecies,
                finalLevel,
                finalTown,
                finalForestArea,
                finalStatus,
                finalOwnership,
                minAge,
                maxAge,
                pageable
        );
    }
}
