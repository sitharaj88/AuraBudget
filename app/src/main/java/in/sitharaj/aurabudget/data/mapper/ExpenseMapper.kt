package `in`.sitharaj.aurabudget.data.mapper

import `in`.sitharaj.aurabudget.data.local.entity.ExpenseEntity as ExpenseDataEntity
import `in`.sitharaj.aurabudget.domain.model.ExpenseEntity as ExpenseDomainEntity

/**
 * Mapper between data layer and domain layer entities
 * Following Single Responsibility Principle and Separation of Concerns
 */
object ExpenseMapper {

    fun mapToDomain(entity: ExpenseDataEntity): ExpenseDomainEntity {
        return ExpenseDomainEntity(
            id = entity.id,
            amount = entity.amount,
            categoryId = entity.categoryId,
            date = entity.date,
            description = entity.description,
            tags = entity.tags.split(",").filter { it.isNotBlank() }
        )
    }

    fun mapToData(domainEntity: ExpenseDomainEntity): ExpenseDataEntity {
        return ExpenseDataEntity(
            id = domainEntity.id,
            amount = domainEntity.amount,
            categoryId = domainEntity.categoryId,
            date = domainEntity.date,
            description = domainEntity.description,
            tags = domainEntity.tags.joinToString(",")
        )
    }

    fun mapToDomainList(entities: List<ExpenseDataEntity>): List<ExpenseDomainEntity> {
        return entities.map { mapToDomain(it) }
    }

    fun mapToDataList(domainEntities: List<ExpenseDomainEntity>): List<ExpenseDataEntity> {
        return domainEntities.map { mapToData(it) }
    }
}
