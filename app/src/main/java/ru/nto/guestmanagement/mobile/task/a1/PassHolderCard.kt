package ru.nto.guestmanagement.mobile.task.a1

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import ru.nto.guestmanagement.mobile.model.PassDetails

private val DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy")
private val TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm")
private val DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")

@Composable
fun PassHolderCard(
    pass: PassDetails,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = pass.visitor.fullName,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.testTag("pass_holder")
            )

            if (!pass.visitor.organization.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = pass.visitor.organization,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.testTag("pass_organization")
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = formatValidPeriod(pass.validFrom, pass.validTo),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.testTag("pass_valid_period")
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = formatEntriesLimit(pass.maxEntries),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.testTag("pass_entries_limit")
            )
        }
    }
}

private fun formatValidPeriod(validFrom: LocalDateTime, validTo: LocalDateTime): String {
    return if (validFrom.toLocalDate() == validTo.toLocalDate()) {
        val date = validFrom.format(DATE_FORMATTER)
        val fromTime = validFrom.format(TIME_FORMATTER)
        val toTime = validTo.format(TIME_FORMATTER)
        "Действует $date, $fromTime\u2013$toTime"
    } else {
        val fromDateTime = validFrom.format(DATE_TIME_FORMATTER)
        val toDateTime = validTo.format(DATE_TIME_FORMATTER)
        "Действует с $fromDateTime по $toDateTime"
    }
}

private fun formatEntriesLimit(maxEntries: Int): String {
    if (maxEntries == 1) return "Разовый пропуск"

    val mod100 = maxEntries % 100
    val mod10 = maxEntries % 10
    val word = when {
        mod100 in 11..14 -> "проходов"
        mod10 == 1 -> "проход"
        mod10 in 2..4 -> "прохода"
        else -> "проходов"
    }
    return "Лимит: $maxEntries $word"
}
