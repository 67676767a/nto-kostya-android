package ru.nto.guestmanagement.mobile.task.a2

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import ru.nto.guestmanagement.mobile.R
import ru.nto.guestmanagement.mobile.data.TimeProvider
import ru.nto.guestmanagement.mobile.model.PassData
import ru.nto.guestmanagement.mobile.model.PassStatus
import ru.nto.guestmanagement.mobile.ui.PassQr
import ru.nto.guestmanagement.mobile.ui.statusColor

@Composable
fun PassAccessPanel(
    data: PassData,
    timeProvider: TimeProvider,
    modifier: Modifier = Modifier
) {
    val now = timeProvider.now()
    val status = calculatePassStatus(data.pass, data.scans, now)

    val (statusText, statusColorRes) = when (status) {
        PassStatus.BLOCKED -> "Заблокирован" to R.color.status_blocked
        PassStatus.NOT_STARTED -> "Ещё не действует" to R.color.status_not_started
        PassStatus.EXPIRED -> "Истёк" to R.color.status_expired
        PassStatus.EXHAUSTED -> "Лимит проходов исчерпан" to R.color.status_exhausted
        PassStatus.ACTIVE -> "Активен" to R.color.status_active
    }

    val color = colorResource(id = statusColorRes)

    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = statusText,
                color = color,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .testTag("pass_status")
                    .statusColor(color)
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (status == PassStatus.ACTIVE) {
                PassQr(data.pass.qrCode)
            } else {
                Text(
                    text = "QR-код недоступен",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.testTag("pass_qr_hidden")
                )
            }
        }
    }
}
