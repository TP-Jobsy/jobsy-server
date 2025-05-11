package com.example.jobsyserver.features.category.controller;

import com.example.jobsyserver.features.category.dto.CategoryDto;
import com.example.jobsyserver.features.category.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Tag(name = "Categories", description = "Операции для работы с категориями")
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "Получить список категорий", description = "Возвращает список всех категорий (общедоступно)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список категорий получен успешно"),
            @ApiResponse(responseCode = "404", description = "Категории не найдены"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<CategoryDto> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @Operation(summary = "Получить категорию по идентификатору", description = "Возвращает категорию по указанному id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Категория получена успешно"),
            @ApiResponse(responseCode = "404", description = "Категория не найдена"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long id) {
        CategoryDto category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    @Operation(summary = "Создать категорию", description = "Создает новую категорию")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Категория создана успешно"),
            @ApiResponse(responseCode = "400", description = "Неверные данные для создания категории"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto) {
        CategoryDto createdCategory = categoryService.createCategory(categoryDto);
        return ResponseEntity.ok(createdCategory);
    }

    @Operation(summary = "Обновить категорию", description = "Обновляет данные категории по указанному id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Категория обновлена успешно"),
            @ApiResponse(responseCode = "400", description = "Неверные данные для обновления категории"),
            @ApiResponse(responseCode = "404", description = "Категория не найдена"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Long id, @RequestBody CategoryDto categoryDto) {
        CategoryDto updatedCategory = categoryService.updateCategory(id, categoryDto);
        return ResponseEntity.ok(updatedCategory);
    }

    @Operation(summary = "Удалить категорию", description = "Удаляет категорию по указанному id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Категория успешно удалена"),
            @ApiResponse(responseCode = "404", description = "Категория не найдена"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategoryById(id);
        return ResponseEntity.noContent().build();
    }
}