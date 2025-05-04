package org.example.project.features.feed.data.mock

import kotlin.random.Random

object FeedTextMock {
    // Короткие заголовки
    private val shortTitles = listOf(
        "Лекция",
        "Семинар",
        "Экзамен"
    )

    // Средние заголовки
    private val mediumTitles = listOf(
        "Лекция по математике",
        "Семинар по программированию",
        "Экзамен по физике"
    )

    // Длинные заголовки
    private val longTitles = listOf(
        "Лекция по дифференциальным уравнениям в частных производных",
        "Семинар по объектно-ориентированному программированию на Java",
        "Итоговый экзамен по квантовой механике и теории поля"
    )

    // Короткие описания
    private val shortDescriptions = listOf(
        "Обязательное посещение.",
        "Принести конспекты.",
        "Подготовить вопросы."
    )

    // Средние описания
    private val mediumDescriptions = listOf(
        "Обязательное посещение. Будет рассмотрена новая тема. Принести ноутбук.",
        "Разбор домашнего задания и решение новых задач. Не забудьте конспекты.",
        "Подготовка к итоговой аттестации. Будут рассмотрены основные вопросы."
    )

    // Длинные описания
    private val longDescriptions = listOf(
        "Обязательное посещение для всех студентов группы. На лекции будет рассмотрена новая тема, которая войдет в экзаменационные вопросы. Необходимо принести ноутбук для выполнения практических заданий и конспект предыдущей лекции.",
        "На семинаре будет проведен детальный разбор домашнего задания, а также решение новых задач повышенной сложности. Рекомендуется заранее ознакомиться с материалами в учебнике на страницах 45-60. Не забудьте принести все необходимые конспекты и калькулятор.",
        "Подготовка к итоговой аттестации включает в себя повторение всех пройденных тем за семестр. Будут рассмотрены наиболее сложные вопросы и типовые задачи, которые могут встретиться на экзамене. Рекомендуется подготовить список вопросов по непонятным темам для обсуждения."
    )

    // Функция для получения случайного заголовка
    fun getRandomTitle(length: TextLength = TextLength.random()): String {
        return when (length) {
            TextLength.SHORT -> shortTitles.random()
            TextLength.MEDIUM -> mediumTitles.random()
            TextLength.LONG -> longTitles.random()
        }
    }

    // Функция для получения случайного описания
    fun getRandomDescription(length: TextLength = TextLength.random()): String {
        return when (length) {
            TextLength.SHORT -> shortDescriptions.random()
            TextLength.MEDIUM -> mediumDescriptions.random()
            TextLength.LONG -> longDescriptions.random()
        }
    }

    enum class TextLength {
        SHORT, MEDIUM, LONG;

        companion object {
            fun random(): TextLength {
                return TextLength.entries[Random.nextInt(TextLength.entries.size)]
            }
        }
    }
}
