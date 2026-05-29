package com.zooeydigital.marsrover.presentation.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import androidx.compose.ui.res.stringResource
import com.zooeydigital.marsrover.R
import com.zooeydigital.marsrover.core.common.resolveMessage
import com.zooeydigital.marsrover.domain.model.MarsPhoto
import com.zooeydigital.marsrover.domain.model.MarsRover
import com.zooeydigital.marsrover.domain.model.RoverCamera
import com.zooeydigital.marsrover.ui.theme.MarsRoverTheme
import java.util.Calendar

@Composable
fun RoverDetailScreen(
    state: RoverDetailScreenState,
    onDateSelected: (String) -> Unit,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val rover = state.rover
    val uiState = state.photosState
    val selectedDate = state.selectedDate

    val onDateClick = {
        val calendar = Calendar.getInstance()
        val parts = selectedDate.split("-")
        if (parts.size == 3) {
            calendar.set(Calendar.YEAR, parts[0].toInt())
            calendar.set(Calendar.MONTH, parts[1].toInt() - 1)
            calendar.set(Calendar.DAY_OF_MONTH, parts[2].toInt())
        }

        val datePickerDialog = android.app.DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val formattedDate = String.format(java.util.Locale.US, "%04d-%02d-%02d", year, month + 1, dayOfMonth)
                onDateSelected(formattedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.show()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (rover == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    RoverDetailHeader(rover)
                }
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Spacer(Modifier.height(8.dp))
                }
                item(span = { GridItemSpan(maxLineSpan) }) {
                    DateInputBox(selectedDate, onDateClick)
                }
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Spacer(Modifier.height(12.dp))
                }

                when (uiState) {
                    PhotosState.Loading -> {
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            DetailLoadingContent()
                        }
                    }
                    is PhotosState.Error -> {
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            val message = when (uiState) {
                                PhotosState.Error.InvalidRover -> stringResource(R.string.error_invalid_rover)
                                is PhotosState.Error.StandardError -> uiState.error.resolveMessage(R.string.unable_to_load_photos)
                            }
                            DetailErrorContent(message, onRetryClick)
                        }
                    }
                    PhotosState.Empty -> {
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            DetailEmptyContent()
                        }
                    }
                    is PhotosState.Success -> {
                        items(uiState.photos, key = { it.id }) { photo ->
                            PhotoCard(photo = photo)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RoverDetailHeader(rover: MarsRover) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = rover.name,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Normal,
        )
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                RoverDetailFact(label = stringResource(R.string.rover_launch), value = formatToUiDate(rover.launchDate))
                Spacer(Modifier.height(4.dp))
                RoverDetailFact(label = stringResource(R.string.rover_landing), value = formatToUiDate(rover.landingDate))
            }
            Column(modifier = Modifier.weight(1f)) {
                RoverDetailFact(label = stringResource(R.string.rover_total_photos), value = rover.totalPhotos.toString())
                Spacer(Modifier.height(4.dp))
                RoverDetailFact(label = stringResource(R.string.rover_cameras_available), value = rover.cameras.size.toString())
            }
        }
    }
}

@Composable
private fun RoverDetailFact(label: String, value: String) {
    Text(
        text = "$label: $value",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
private fun DateInputBox(selectedDate: String, onDateClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onDateClick() }
    ) {
        OutlinedTextField(
            value = formatToUiDate(selectedDate),
            onValueChange = {},
            label = { Text(stringResource(R.string.date_input_label)) },
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = stringResource(R.string.select_date_label)
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledBorderColor = MaterialTheme.colorScheme.outline,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            enabled = false
        )
    }
}

@Composable
private fun PhotoCard(
    photo: MarsPhoto,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
    ) {
        SubcomposeAsyncImage(
            model = photo.imageUrl,
            contentDescription = stringResource(R.string.mars_rover_photo_desc),
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            loading = {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            },
            error = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.errorContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = stringResource(R.string.failed_to_load_photo),
                        tint = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        )
    }
}

@Composable
private fun DetailLoadingContent() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
private fun DetailEmptyContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp, horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(R.string.no_photos_taken_title),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.no_photos_taken_desc),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun DetailErrorContent(
    message: String,
    onRetryClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp, horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(R.string.unable_to_load_photos),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = onRetryClick,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(stringResource(R.string.retry_label))
        }
    }
}

private fun formatToUiDate(apiDate: String): String {
    val parts = apiDate.split("-")
    return if (parts.size == 3) {
        "${parts[1]}/${parts[2]}/${parts[0]}"
    } else {
        apiDate
    }
}

@Preview(showBackground = true)
@Composable
private fun RoverDetailScreenPreview() {
    MarsRoverTheme {
        RoverDetailScreen(
            state = RoverDetailScreenState(
                rover = MarsRover(
                    id = "curiosity",
                    name = "Curiosity",
                    landingDate = "2012-08-06",
                    launchDate = "2011-11-26",
                    totalPhotos = 695670,
                    cameras = listOf(
                        RoverCamera("FHAZ", "Front Hazard Avoidance Camera")
                    ),
                    maxDate = "2026-05-27",
                ),
                selectedDate = "2023-08-17",
                photosState = PhotosState.Success(
                    photos = listOf(
                        MarsPhoto(
                            id = "1",
                            imageUrl = "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/03921/opgs/edr/ncam/NLB_745585765EDR_F1031528NCAM00354M_.JPG",
                            fullResUrl = "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/03921/opgs/edr/ncam/NLB_745585765EDR_F1031528NCAM00354M_.JPG"
                        )
                    )
                )
            ),
            onDateSelected = {},
            onRetryClick = {}
        )
    }
}
