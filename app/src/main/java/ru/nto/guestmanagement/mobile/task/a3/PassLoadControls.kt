package ru.nto.guestmanagement.mobile.task.a3

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import ru.nto.guestmanagement.mobile.model.PassScreenState

@Composable
fun PassLoadControls(
    state: PassScreenState,
    onRefresh: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (state) {
            is PassScreenState.InitialLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.testTag("pass_loading")
                )
            }

            is PassScreenState.InitialError -> {
                Text(
                    text = state.message,
                    modifier = Modifier.testTag("pass_error")
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = onRetry,
                    modifier = Modifier.testTag("pass_retry")
                ) {
                    Text(text = "Повторить")
                }
            }

            is PassScreenState.Content -> {
                if (state.isRefreshing) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.testTag("pass_loading")
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Button(
                            onClick = onRefresh,
                            enabled = false,
                            modifier = Modifier.testTag("pass_refresh")
                        ) {
                            Text(text = "Обновить")
                        }
                    }
                } else {
                    if (state.isOffline) {
                        Text(
                            text = "Нет связи",
                            modifier = Modifier.testTag("pass_offline")
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    Button(
                        onClick = onRefresh,
                        enabled = true,
                        modifier = Modifier.testTag("pass_refresh")
                    ) {
                        Text(text = "Обновить")
                    }
                }
            }
        }
    }
}
