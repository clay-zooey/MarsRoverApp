package com.zooeydigital.marsrover.presentation.rovers

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zooeydigital.marsrover.R
import com.zooeydigital.marsrover.domain.model.MarsRover
import com.zooeydigital.marsrover.domain.model.RoverCamera
import com.zooeydigital.marsrover.ui.theme.MarsRoverTheme

@Composable
fun RoversScreen(
    uiState: RoversUiState,
    onRoverClick: (MarsRover) -> Unit,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when (uiState) {
            RoversUiState.Empty -> EmptyContent(onRetryClick)
            is RoversUiState.Error -> ErrorContent(uiState.message, onRetryClick)
            RoversUiState.Loading -> LoadingContent()
            is RoversUiState.Success -> RoverList(uiState.rovers, onRoverClick)
        }
    }
}

@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
        )
    }
}

@Composable
private fun EmptyContent(onRetryClick: () -> Unit) {
    MessageContent(
        title = stringResource(R.string.no_rovers_found),
        body = stringResource(R.string.no_rovers_found_desc),
        actionLabel = stringResource(R.string.retry_label),
        onActionClick = onRetryClick,
    )
}

@Composable
private fun ErrorContent(
    message: String,
    onRetryClick: () -> Unit,
) {
    MessageContent(
        title = stringResource(R.string.unable_to_load_rovers),
        body = message,
        actionLabel = stringResource(R.string.retry_label),
        onActionClick = onRetryClick,
    )
}

@Composable
private fun MessageContent(
    title: String,
    body: String,
    actionLabel: String,
    onActionClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = body,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(20.dp))
        Button(
            onClick = onActionClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ),
        ) {
            Text(actionLabel)
        }
    }
}

@Composable
private fun RoverList(
    rovers: List<MarsRover>,
    onRoverClick: (MarsRover) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = 16.dp,
            top = 24.dp,
            end = 16.dp,
            bottom = 24.dp,
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Header()
        }
        items(
            items = rovers,
            key = { rover -> rover.id },
        ) { rover ->
            RoverCard(rover, onRoverClick)
        }
    }
}

@Composable
private fun Header() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
    ) {
        Text(
            text = stringResource(R.string.nasa_mars_rovers_title),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Normal,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.nasa_mars_rovers_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}

@Composable
private fun RoverCard(
    rover: MarsRover,
    onRoverClick: (MarsRover) -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Image(
                painter = painterResource(id = getRoverDrawableRes(rover.id)),
                contentDescription = stringResource(R.string.rover_surface_image_description, rover.name),
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.95f),
                contentScale = ContentScale.Crop,
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.Top,
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                ) {
                    Text(
                        text = rover.name,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Normal,
                    )
                    Spacer(Modifier.height(10.dp))
                    RoverDetail(label = stringResource(R.string.rover_launch), value = rover.launchDate)
                    RoverDetail(label = stringResource(R.string.rover_landing), value = rover.landingDate)
                    RoverDetail(label = stringResource(R.string.rover_total_photos), value = rover.totalPhotos.toString())
                    RoverDetail(label = stringResource(R.string.rover_cameras_available), value = rover.cameras.size.toString())
                }
                Spacer(Modifier.width(12.dp))
                TextButton(
                    onClick = { onRoverClick(rover) },
                    contentPadding = PaddingValues(horizontal = 0.dp, vertical = 4.dp),
                ) {
                    Text(
                        text = stringResource(R.string.view_images_cta),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
    }
}

@Composable
private fun RoverDetail(
    label: String,
    value: String,
) {
    Text(
        text = "$label: $value",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

@Preview(showBackground = true)
@Composable
private fun RoversScreenPreview() {
    MarsRoverTheme {
        RoversScreen(
            uiState = RoversUiState.Success(
                rovers = listOf(
                    MarsRover(
                        id = "curiosity",
                        name = "Curiosity",
                        landingDate = "2012-08-06",
                        launchDate = "2011-11-26",
                        totalPhotos = 695670,
                        cameras = listOf(
                            RoverCamera("FHAZ", "Front Hazard Avoidance Camera"),
                            RoverCamera("MAST", "Mast Camera"),
                            RoverCamera("NAVCAM", "Navigation Camera"),
                        ),
                        maxDate = "2026-05-27",
                    ),
                    MarsRover(
                        id = "spirit",
                        name = "Spirit",
                        landingDate = "2004-01-04",
                        launchDate = "2003-06-10",
                        totalPhotos = 124550,
                        cameras = listOf(
                            RoverCamera("FHAZ", "Front Hazard Avoidance Camera"),
                            RoverCamera("NAVCAM", "Navigation Camera"),
                        ),
                        maxDate = "2010-03-02",
                    ),
                    MarsRover(
                        id = "opportunity",
                        name = "Opportunity",
                        landingDate = "2004-01-25",
                        launchDate = "2003-07-07",
                        totalPhotos = 198439,
                        cameras = listOf(
                            RoverCamera("PANCAM", "Panoramic Camera"),
                            RoverCamera("MINITES", "Miniature Thermal Emission Spectrometer"),
                        ),
                        maxDate = "2018-06-10",
                    ),
                ),
            ),
            onRoverClick = {},
            onRetryClick = {},
        )
    }
}

private fun getRoverDrawableRes(roverId: String): Int {
    return when (roverId.lowercase()) {
        "curiosity" -> R.drawable.curiousity_banner
        "opportunity" -> R.drawable.opportunity_banner
        "perseverance" -> R.drawable.perseverance_banner
        "spirit" -> R.drawable.spirit_banner
        else -> R.drawable.curiousity_banner
    }
}
