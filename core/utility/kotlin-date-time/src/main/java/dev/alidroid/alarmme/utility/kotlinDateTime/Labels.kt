package dev.alidroid.alarmme.utility.kotlinDateTime

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month

private const val EVERY_DAY_LABEL = "Every day"
private const val RANGE_SEPARATOR = "–"
private const val ITEM_SEPARATOR = ", "
private const val GROUP_SEPARATOR = " · "

/** A run this long or longer reads better as "Mon–Thu" than as "Mon, Tue, Wed, Thu". */
private const val MIN_RUN_FOR_RANGE = 3

/** How many characters of a `MONDAY`/`SEPTEMBER`-style enum name make its short label. */
private const val SHORT_LABEL_LENGTH = 3

/**
 * `"Mon–Fri"`, `"Mon, Wed, Fri"`, `"Every day"` — or `""` when empty, leaving it to the caller to
 * decide how "never repeats" should read in its own context.
 *
 * English-only, like the rest of the app's date formatting.
 */
fun Set<DayOfWeek>.toRangeLabel(): String {
    if (isEmpty()) return ""
    if (size == DayOfWeek.entries.size) return EVERY_DAY_LABEL

    return sortedBy { it.ordinal }
        .consecutiveRuns()
        .joinToString(ITEM_SEPARATOR) { run ->
            if (run.size >= MIN_RUN_FOR_RANGE) {
                run.first().shortLabel() + RANGE_SEPARATOR + run.last().shortLabel()
            } else {
                run.joinToString(ITEM_SEPARATOR) { it.shortLabel() }
            }
        }
}

/** Splits an ascending list into runs of consecutive days, so each can collapse to a range. */
private fun List<DayOfWeek>.consecutiveRuns(): List<List<DayOfWeek>> {
    val runs = mutableListOf<MutableList<DayOfWeek>>()

    forEach { day ->
        val currentRun = runs.lastOrNull()
        if (currentRun != null && day.ordinal == currentRun.last().ordinal + 1) {
            currentRun += day
        } else {
            runs += mutableListOf(day)
        }
    }

    return runs
}

/**
 * `"Sep 20, 24, 28"` — grouped by month so a month name is never repeated, `"Sep 28 · Oct 2"`
 * across a boundary. `""` when empty.
 */
fun List<LocalDate>.toGroupedLabel(): String {
    if (isEmpty()) return ""

    return sorted()
        .groupBy { it.month }
        .entries
        .joinToString(GROUP_SEPARATOR) { (month, dates) ->
            month.shortLabel() + " " + dates.joinToString(ITEM_SEPARATOR) { it.day.toString() }
        }
}

private fun DayOfWeek.shortLabel(): String = name.toShortLabel()

private fun Month.shortLabel(): String = name.toShortLabel()

private fun String.toShortLabel(): String =
    take(SHORT_LABEL_LENGTH).lowercase().replaceFirstChar { it.uppercase() }
