package com.example.uikit.data

data class Material(
    val id: Int,
    val name: String,
    val category: String,
    val description: String = "",
    val colors: List<MaterialColor> = emptyList()
)

data class MaterialColor(
    val name: String,
    val hexCode: String
)

data class Facade(
    val id: Int,
    val name: String,
    val description: String = ""
)

data class Hardware(
    val id: Int,
    val name: String,
    val description: String = ""
)

object KoronaCatalog {
    val materials = listOf(
        Material(1, "ЛДСП Egger (Австрия)", "ЛДСП", "Высококачественный ЛДСП от австрийского производителя. Класс E1.", listOf(
            MaterialColor("Белый", "#FFFFFF"), MaterialColor("Дуб Сонома", "#C4A882"),
            MaterialColor("Венге", "#3B2F2F"), MaterialColor("Дуб Милан", "#8B7355"),
            MaterialColor("Чёрный", "#000000"), MaterialColor("Орех", "#5C4033"),
            MaterialColor("Серый бетон", "#808080"), MaterialColor("Бежевый", "#F5F5DC")
        )),
        Material(2, "МДФ Эмаль (Россия)", "МДФ", "МДФ с эмалевым покрытием. Глянцевые и матовые варианты.", listOf(
            MaterialColor("Белый глянец", "#F0F0F0"), MaterialColor("Шампань", "#F7E7CE"),
            MaterialColor("Графит", "#36454F"), MaterialColor("Антрацит", "#293133"),
            MaterialColor("Слоновая кость", "#FFFFF0"), MaterialColor("Мокрый асфальт", "#505050")
        )),
        Material(3, "Массив дуба (Россия)", "Массив", "Натуральный массив дуба высшего сорта.", listOf(
            MaterialColor("Натуральный", "#C4A882"), MaterialColor("Белёный", "#E8DCC8"),
            MaterialColor("Тёмный", "#6B4226"), MaterialColor("Винтаж", "#A0794E")
        )),
        Material(4, "МДФ Плёнка PVC", "МДФ", "МДФ с плёночным покрытием. Влагостойкий.", listOf(
            MaterialColor("Белый матовый", "#F5F5F5"), MaterialColor("Дуб Кантри", "#B89B72"),
            MaterialColor("Орех Миланский", "#7B5B3A"), MaterialColor("Альпийский дуб", "#D4C5A9")
        )),
        Material(5, "ЛДСП Kronospan", "ЛДСП", "ЛДСП от мирового лидера. Широкий выбор декоров.", listOf(
            MaterialColor("Дуб Шервуд", "#8B6914"), MaterialColor("Грецкий орех", "#5E3A29"),
            MaterialColor("Бетон", "#B0B0B0"), MaterialColor("Белый кристалл", "#E8E8E8"),
            MaterialColor("Ясень шимо", "#C0A090")
        )),
        Material(6, "Фанера ФК (Россия)", "Фанера", "Берёзовая фанера высшего сорта для корпусов.", listOf(
            MaterialColor("Натуральная", "#D4A76A"), MaterialColor("Тонированная", "#8B6508")
        ))
    )

    val facades = listOf(
        Facade(1, "Гладкий (классика)", "Прямые фасады без фрезеровки"),
        Facade(2, "П-образный", "Фасад с П-образной фрезеровкой"),
        Facade(3, "Рамочный", "Фасад с рамкой и филёнкой"),
        Facade(4, "Фигурный (R)", "Фасад с радиусным скруглением"),
        Facade(5, "Витрина", "Фасад со стеклянной вставкой"),
        Facade(6, "Реечный", "Фасад с вертикальными рейками")
    )

    val hardware = listOf(
        Hardware(1, "Hettich (Германия)", "Премиум-фурнитура"),
        Hardware(2, "Blum (Австрия)", "Направляющие и петли премиум"),
        Hardware(3, "Boyard (Китай)", "Бюджетная фурнитура"),
        Hardware(4, "GTV (Польша)", "Средний ценовой сегмент"),
        Hardware(5, "РосФурнитура", "Отечественная фурнитура")
    )
}
