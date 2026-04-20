package com.example.ancienttreesystem.controller;

import com.example.ancienttreesystem.entity.AncientTree;
import com.example.ancienttreesystem.service.AncientTreeService;
import com.example.ancienttreesystem.util.ExcelUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 古树名木控制器
 * 提供 REST API 接口
 */
@RestController
@RequestMapping("/api/trees")
@RequiredArgsConstructor
public class AncientTreeController {

    private final AncientTreeService ancientTreeService;

    // ========== 1. 分页查询（默认接口）==========

    /**
     * 分页查询所有古树（真分页）- 默认接口
     * GET /api/trees?page=0&size=10
     */
    @GetMapping  // ← 不写路径，默认就是 /api/trees
    public Map<String, Object> findAllByPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<AncientTree> result = ancientTreeService.findAllByPage(page, size);

        Map<String, Object> response = new HashMap<>();
        response.put("content", result.getContent());
        response.put("totalElements", result.getTotalElements());
        response.put("totalPages", result.getTotalPages());
        response.put("currentPage", result.getNumber());
        response.put("pageSize", result.getSize());
        response.put("hasNext", result.hasNext());
        response.put("hasPrevious", result.hasPrevious());

        return response;
    }

    // ========== 2. 查询所有（不分页）==========

    /**
     * 查询所有古树（不分页，保留给旧版调用）
     * GET /api/trees/all
     */
    @GetMapping("/all")  // ← 明确指定路径 /api/trees/all
    public List<AncientTree> findAll() {
        return ancientTreeService.findAll();
    }

    // ========== 3. 根据ID查询 ==========

    /**
     * 根据ID查询古树
     * GET /api/trees/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<AncientTree> findById(@PathVariable Long id) {
        return ancientTreeService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ========== 4. 新增、更新、删除 ==========

    @PostMapping
    public AncientTree save(@RequestBody AncientTree tree) {
        return ancientTreeService.save(tree);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AncientTree> update(@PathVariable Long id, @RequestBody AncientTree tree) {
        if (!ancientTreeService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        tree.setId(id);
        return ResponseEntity.ok(ancientTreeService.save(tree));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        if (!ancientTreeService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        ancientTreeService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    // ========== 5. 其他查询接口（保持不变）==========

    @GetMapping("/species")
    public List<AncientTree> findBySpecies(@RequestParam String species) {
        return ancientTreeService.findBySpecies(species);
    }

    @GetMapping("/level")
    public List<AncientTree> findByProtectionLevel(@RequestParam String level) {
        return ancientTreeService.findByProtectionLevel(level);
    }

    @GetMapping("/town")
    public List<AncientTree> findByTown(@RequestParam String town) {
        return ancientTreeService.findByTown(town);
    }

    @GetMapping("/search")
    public List<AncientTree> search(@RequestParam String keyword) {
        return ancientTreeService.searchBySpeciesKeyword(keyword);
    }

    @GetMapping("/old")
    public List<AncientTree> findOldTrees(@RequestParam Integer minAge) {
        return ancientTreeService.findOldTrees(minAge);
    }

    @GetMapping("/age-range")
    public List<AncientTree> findByAgeRange(@RequestParam Integer min, @RequestParam Integer max) {
        return ancientTreeService.findByAgeRange(min, max);
    }

    @GetMapping("/count")
    public long count() {
        return ancientTreeService.count();
    }

    // ========== 6. 分页搜索接口 ==========

    /**
     * 分页 + 模糊搜索树种
     * GET /api/trees/page-search?keyword=樟&page=0&size=10
     */
    @GetMapping("/page-search")
    public Map<String, Object> searchByPage(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<AncientTree> result = ancientTreeService.searchBySpeciesPage(keyword, page, size);

        Map<String, Object> response = new HashMap<>();
        response.put("content", result.getContent());
        response.put("totalElements", result.getTotalElements());
        response.put("totalPages", result.getTotalPages());
        response.put("currentPage", result.getNumber());
        response.put("pageSize", result.getSize());
        response.put("hasNext", result.hasNext());
        response.put("hasPrevious", result.hasPrevious());

        return response;
    }

    // ========== 6. 新增：高级搜索接口 ==========

    /**
     * 高级搜索 - 多条件组合查询（完整版）
     * GET /api/trees/advanced-search?species=樟树&level=一级&minAge=100&page=0&size=10
     * 所有参数都是可选的，不传则不限制该条件
     */
    @GetMapping("/advanced-search")
    public Map<String, Object> advancedSearch(
            @RequestParam(required = false) String species,
            @RequestParam(required = false) String level,
            @RequestParam(required = false) String town,
            @RequestParam(required = false) String forestArea,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String ownership,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        // 调用Service进行高级搜索
        Page<AncientTree> result = ancientTreeService.advancedSearch(
                species,
                level,
                town,
                forestArea,
                status,
                ownership,
                minAge,
                maxAge,
                page,
                size
        );

        // 组装返回数据
        Map<String, Object> response = new HashMap<>();
        response.put("content", result.getContent());
        response.put("totalElements", result.getTotalElements());
        response.put("totalPages", result.getTotalPages());
        response.put("currentPage", result.getNumber());
        response.put("pageSize", result.getSize());
        response.put("hasNext", result.hasNext());
        response.put("hasPrevious", result.hasPrevious());

        return response;
    }

    /**
     * 导出 Excel
     * GET /api/trees/export
     */
    @GetMapping("/export")
    public void exportExcel(HttpServletResponse response) throws IOException {
        // 查询所有数据
        List<AncientTree> trees = ancientTreeService.findAll();

        // 导出到 Excel
        ExcelUtil.exportTreesToExcel(trees, response);
    }

    /**
     * 导入 Excel
     * POST /api/trees/import
     */
    @PostMapping("/import")
    public ResponseEntity<Map<String, Object>> importExcel(@RequestParam("file") MultipartFile file) {
        Map<String, Object> result = new HashMap<>();

        // 检查文件是否为空
        if (file.isEmpty()) {
            result.put("success", false);
            result.put("message", "文件不能为空");
            return ResponseEntity.badRequest().body(result);
        }

        // 检查文件类型
        String fileName = file.getOriginalFilename();
        if (fileName == null || (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls"))) {
            result.put("success", false);
            result.put("message", "请上传Excel文件（.xlsx或.xls）");
            return ResponseEntity.badRequest().body(result);
        }

        try {
            // 读取Excel数据
            List<AncientTree> trees = ExcelUtil.importTreesFromExcel(file.getInputStream());

            // 保存到数据库
            int successCount = 0;
            int failCount = 0;
            StringBuilder failMsg = new StringBuilder();

            for (int i = 0; i < trees.size(); i++) {
                AncientTree tree = trees.get(i);
                try {
                    // 简单验证：树种不能为空
                    if (tree.getSpecies() == null || tree.getSpecies().isEmpty()) {
                        failCount++;
                        failMsg.append("第").append(i + 2).append("行：树种不能为空；");
                        continue;
                    }

                    ancientTreeService.save(tree);
                    successCount++;
                } catch (Exception e) {
                    failCount++;
                    failMsg.append("第").append(i + 2).append("行：").append(e.getMessage()).append("；");
                }
            }

            result.put("success", true);
            result.put("successCount", successCount);
            result.put("failCount", failCount);
            result.put("message", failMsg.length() > 0 ? failMsg.toString() : "全部导入成功");

            return ResponseEntity.ok(result);

        } catch (IOException e) {
            result.put("success", false);
            result.put("message", "文件读取失败：" + e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }
}