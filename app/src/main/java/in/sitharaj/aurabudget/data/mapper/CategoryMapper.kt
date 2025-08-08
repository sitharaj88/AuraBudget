package `in`.sitharaj.aurabudget.data.mapper

import `in`.sitharaj.aurabudget.data.local.entity.CategoryEntity as CategoryDataEntity
import `in`.sitharaj.aurabudget.domain.model.CategoryEntity as CategoryDomainEntity

/**
 * Mapper between data layer and domain layer entities for Category
 * Following Single Responsibility Principle and Separation of Concerns
 */
object CategoryMapper {

    fun mapToDomain(entity: CategoryDataEntity): CategoryDomainEntity {
        return CategoryDomainEntity(
            id = entity.id,
            name = entity.name,
            icon = entity.icon,
            color = entity.color?.toLongOrNull(),
            type = CategoryDomainEntity.CategoryType.valueOf(entity.type),
            isDefault = entity.isDefault,
            isActive = entity.isActive ?: true,
            usageCount = entity.usageCount ?: 0,
            monthlyBudget = entity.monthlyBudget
        )
    }

    fun mapToData(domainEntity: CategoryDomainEntity): CategoryDataEntity {
        return CategoryDataEntity(
            id = domainEntity.id,
            name = domainEntity.name,
            icon = domainEntity.icon,
            color = domainEntity.color?.toString(),
            type = domainEntity.type.name,
            isDefault = domainEntity.isDefault,
            isActive = domainEntity.isActive,
            usageCount = domainEntity.usageCount,
            monthlyBudget = domainEntity.monthlyBudget
        )
    }

    fun mapToDomainList(entities: List<CategoryDataEntity>): List<CategoryDomainEntity> {
        return entities.map { mapToDomain(it) }
    }

    fun mapToDataList(domainEntities: List<CategoryDomainEntity>): List<CategoryDataEntity> {
        return domainEntities.map { mapToData(it) }
    }
}
