package `in`.sitharaj.aurabudget.data.mapper

import `in`.sitharaj.aurabudget.data.local.entity.BudgetEntity as BudgetDataEntity
import `in`.sitharaj.aurabudget.domain.model.BudgetEntity as BudgetDomainEntity

/**
 * Mapper between data layer and domain layer entities for Budget
 * Following Single Responsibility Principle and Separation of Concerns
 */
object BudgetMapper {

    fun mapToDomain(entity: BudgetDataEntity): BudgetDomainEntity {
        return BudgetDomainEntity(
            id = entity.id,
            name = entity.name,
            amount = entity.amount,
            spent = entity.spent,
            categoryId = entity.categoryId,
            startDate = entity.startDate,
            endDate = entity.endDate,
            isRecurring = entity.isRecurring
        )
    }

    fun mapToData(domainEntity: BudgetDomainEntity): BudgetDataEntity {
        return BudgetDataEntity(
            id = domainEntity.id,
            name = domainEntity.name,
            amount = domainEntity.amount,
            spent = domainEntity.spent,
            categoryId = domainEntity.categoryId,
            startDate = domainEntity.startDate,
            endDate = domainEntity.endDate,
            isRecurring = domainEntity.isRecurring
        )
    }

    fun mapToDomainList(entities: List<BudgetDataEntity>): List<BudgetDomainEntity> {
        return entities.map { mapToDomain(it) }
    }

    fun mapToDataList(domainEntities: List<BudgetDomainEntity>): List<BudgetDataEntity> {
        return domainEntities.map { mapToData(it) }
    }
}
