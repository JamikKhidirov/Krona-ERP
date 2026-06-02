package com.example.manager.data

import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName




@IgnoreExtraProperties  // ✅ Игнорируем лишние поля из Firestore
data class Order(
    // === БАЗОВЫЕ ПОЛЯ ===
    @PropertyName("id") val id: String = "",
    @PropertyName("userId") val userId: String = "",
    @PropertyName("managerId") val managerId: String = "",           // ID назначенного менеджера
    @PropertyName("managerName") val managerName: String = "",       // Имя менеджера

    // === ТИП ПРОДУКТА ===
    @PropertyName("productTypeId") val productTypeId: Int = 1,
    @PropertyName("productTypeName") val productTypeName: String = "",
    @PropertyName("title") val title: String = "",
    @PropertyName("description") val description: String = "",

    // === РАЗМЕРЫ ===
    @PropertyName("widthCm") val widthCm: String = "",
    @PropertyName("heightCm") val heightCm: String = "",
    @PropertyName("depthCm") val depthCm: String = "",
    @PropertyName("areaSqM") val areaSqM: String = "",                 // Площадь в кв.м

    // === ФИНАНСЫ ===
    @PropertyName("budget") val budget: String = "",
    @PropertyName("paidAmount") val paidAmount: String = "0",
    @PropertyName("costPrice") val costPrice: String = "0",           // Себестоимость
    @PropertyName("profit") val profit: String = "0",                 // Прибыль
    @PropertyName("paymentStatus") val paymentStatus: String = "UNPAID", // UNPAID, PARTIAL, PAID, OVERPAID

    // === МАТЕРИАЛЫ И ЦВЕТ ===
    @PropertyName("material") val material: String = "",
    @PropertyName("materialId") val materialId: String = "",          // ID материала из каталога
    @PropertyName("color") val color: String = "",
    @PropertyName("colorCode") val colorCode: String = "",             // HEX код цвета
    @PropertyName("facade") val facade: String = "",
    @PropertyName("facadeId") val facadeId: String = "",
    @PropertyName("hardware") val hardware: String = "",              // Фурнитура
    @PropertyName("hardwareId") val hardwareId: String = "",

    // === ФОТО ===
    @PropertyName("imageUrls") val imageUrls: List<String> = emptyList(),
    @PropertyName("sketchUrls") val sketchUrls: List<String> = emptyList(),    // Эскизы
    @PropertyName("blueprintUrls") val blueprintUrls: List<String> = emptyList(), // Чертежи
    @PropertyName("photoBeforeUrls") val photoBeforeUrls: List<String> = emptyList(), // Фото до
    @PropertyName("photoAfterUrls") val photoAfterUrls: List<String> = emptyList(),     // Фото после

    // === СТАТУС И ПРИОРИТЕТ ===
    @PropertyName("status") val status: String = "PENDING",
    @PropertyName("priority") val priority: String = "NORMAL",        // LOW, NORMAL, HIGH, URGENT
    @PropertyName("previousStatus") val previousStatus: String = "",  // Предыдущий статус

    // === ДАТЫ ===
    @PropertyName("createdAt") val createdAt: Long = System.currentTimeMillis(),
    @PropertyName("updatedAt") val updatedAt: Long = System.currentTimeMillis(),
    @PropertyName("deadline") val deadline: String = "",
    @PropertyName("deadlineTimestamp") val deadlineTimestamp: Long = 0,
    @PropertyName("assignedAt") val assignedAt: Long = 0,              // Когда назначен менеджер
    @PropertyName("startedAt") val startedAt: Long = 0,              // Когда начат
    @PropertyName("completedAt") val completedAt: Long = 0,          // Когда завершён
    @PropertyName("deliveredAt") val deliveredAt: Long = 0,          // Когда доставлен
    @PropertyName("warrantyUntil") val warrantyUntil: Long = 0,      // Гарантия до

    // === КЛИЕНТ ===
    @PropertyName("clientName") val clientName: String = "",
    @PropertyName("clientPhone") val clientPhone: String = "",
    @PropertyName("clientEmail") val clientEmail: String = "",
    @PropertyName("clientId") val clientId: String = "",

    // === АДРЕС ===
    @PropertyName("address") val address: String = "",
    @PropertyName("city") val city: String = "",
    @PropertyName("district") val district: String = "",
    @PropertyName("entrance") val entrance: String = "",
    @PropertyName("floor") val floor: String = "",
    @PropertyName("apartment") val apartment: String = "",
    @PropertyName("intercom") val intercom: String = "",

    // === МАСТЕР ===
    @PropertyName("masterId") val masterId: String = "",
    @PropertyName("masterName") val masterName: String = "",
    @PropertyName("masterPhone") val masterPhone: String = "",
    @PropertyName("masterPhotoUrl") val masterPhotoUrl: String = "",
    @PropertyName("masterRating") val masterRating: Double = 0.0,
    @PropertyName("masterSpecialty") val masterSpecialty: String = "",

    // === ДОСТАВКА ===
    @PropertyName("deliveryType") val deliveryType: String = "SELF_PICKUP", // SELF_PICKUP, DELIVERY, ASSEMBLY
    @PropertyName("deliveryCost") val deliveryCost: String = "0",
    @PropertyName("deliveryDate") val deliveryDate: String = "",
    @PropertyName("deliveryTime") val deliveryTime: String = "",
    @PropertyName("assemblyRequired") val assemblyRequired: Boolean = false,
    @PropertyName("assemblyCost") val assemblyCost: String = "0",

    // === КОММЕНТАРИИ ===
    @PropertyName("comment") val comment: String = "",                  // Комментарий клиента
    @PropertyName("managerComment") val managerComment: String = "",    // Комментарий менеджера
    @PropertyName("masterComment") val masterComment: String = "",      // Комментарий мастера
    @PropertyName("internalNotes") val internalNotes: String = "",    // Внутренние заметки

    // === ИСТОРИЯ ===
    @PropertyName("historyCount") val historyCount: Int = 0,
    @PropertyName("revisionCount") val revisionCount: Int = 0,         // Количество доработок

    // === РЕЙТИНГ И ОТЗЫВ ===
    @PropertyName("clientRating") val clientRating: Int = 0,           // Оценка клиента
    @PropertyName("clientReview") val clientReview: String = "",
    @PropertyName("reviewDate") val reviewDate: Long = 0,

    // === ИСТОЧНИК ===
    @PropertyName("source") val source: String = "APP",                // APP, SITE, PHONE, WALK_IN, REFERRAL
    @PropertyName("referralCode") val referralCode: String = "",

    // === МЕТА ===
    @PropertyName("isArchived") val isArchived: Boolean = false,
    @PropertyName("archivedAt") val archivedAt: Long = 0,
    @PropertyName("isDeleted") val isDeleted: Boolean = false,
    @PropertyName("deletedAt") val deletedAt: Long = 0,
    @PropertyName("deletedBy") val deletedBy: String = ""
) {
    // === ФУНКЦИИ ===

    /** Оставшаяся сумма к оплате */
    fun getRemainingAmount(): String {
        val budgetVal = budget.extractNumber()
        val paidVal = paidAmount.extractNumber()
        return (budgetVal - paidVal).formatMoney()
    }

    /** Процент оплаты */
    fun getPaymentPercent(): Int {
        val budgetVal = budget.extractNumber()
        val paidVal = paidAmount.extractNumber()
        return if (budgetVal > 0) ((paidVal * 100) / budgetVal).toInt() else 0
    }

    /** Прибыль рассчитанная */
    fun getCalculatedProfit(): String {
        val budgetVal = budget.extractNumber()
        val costVal = costPrice.extractNumber()
        val deliveryVal = deliveryCost.extractNumber()
        val assemblyVal = assemblyCost.extractNumber()
        return (budgetVal - costVal - deliveryVal - assemblyVal).formatMoney()
    }

    /** Маржинальность в процентах */
    fun getMarginPercent(): Int {
        val budgetVal = budget.extractNumber()
        val profitVal = getCalculatedProfit().extractNumber()
        return if (budgetVal > 0) ((profitVal * 100) / budgetVal).toInt() else 0
    }

    /** Полный адрес одной строкой */
    fun getFullAddress(): String {
        return buildString {
            if (city.isNotBlank()) append("$city, ")
            if (district.isNotBlank()) append("$district, ")
            append(address)
            if (entrance.isNotBlank()) append(", подъезд $entrance")
            if (floor.isNotBlank()) append(", этаж $floor")
            if (apartment.isNotBlank()) append(", кв. $apartment")
        }
    }

    /** Дней до дедлайна */
    fun getDaysUntilDeadline(): Long {
        if (deadlineTimestamp == 0L) return -1
        val diff = deadlineTimestamp - System.currentTimeMillis()
        return if (diff > 0) diff / (24 * 60 * 60 * 1000) else 0
    }

    fun isOverdue(): Boolean {
        return deadlineTimestamp > 0 && deadlineTimestamp < System.currentTimeMillis() &&
                status !in listOf("COMPLETED", "PAID", "DELIVERED", "CANCELLED")
    }

    fun isEditable(): Boolean {
        return status !in listOf("COMPLETED", "PAID", "DELIVERED", "CANCELLED", "ARCHIVED")
    }

    fun canAssignMaster(): Boolean {
        return status in listOf("ASSIGNED", "IN_PROGRESS") && masterId.isBlank()
    }

    /** Полная стоимость с доставкой и сборкой */
    fun getTotalCost(): String {
        val budgetVal = budget.extractNumber()
        val deliveryVal = deliveryCost.extractNumber()
        val assemblyVal = assemblyCost.extractNumber()
        return (budgetVal + deliveryVal + assemblyVal).formatMoney()
    }

    /** Краткая сводка для списка */
    fun getShortSummary(): String {
        return "$productTypeName · ${widthCm}×${heightCm}×${depthCm} см · $material"
    }

    /** Проверка полноты данных */
    fun getCompletenessPercent(): Int {
        var filled = 0
        var total = 10
        if (description.isNotBlank()) filled++
        if (budget.isNotBlank()) filled++
        if (material.isNotBlank()) filled++
        if (color.isNotBlank()) filled++
        if (deadline.isNotBlank()) filled++
        if (address.isNotBlank()) filled++
        if (clientPhone.isNotBlank()) filled++
        if (imageUrls.isNotEmpty()) filled++
        if (widthCm.isNotBlank() && heightCm.isNotBlank()) filled++
        if (managerId.isNotBlank()) filled++
        return (filled * 100) / total
    }

    companion object {
        val ACTIVE_STATUSES = listOf("PENDING", "ASSIGNED", "IN_PROGRESS", "READY", "DELIVERING")
        val FINAL_STATUSES = listOf("COMPLETED", "PAID", "CANCELLED")
    }
}

// === РАСШИРЕНИЯ ===

/** Извлечь число из строки с форматированием */
private fun String.extractNumber(): Long {
    return this.replace(Regex("[^0-9]"), "").toLongOrNull() ?: 0
}

/** Форматировать число как деньги */
private fun Long.formatMoney(): String {
    return String.format("%,d", this).replace(",", " ") + " ₽"
}