package com.zooeydigital.marsrover

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.zooeydigital.marsrover.presentation.detail.RoverDetailRoute
import com.zooeydigital.marsrover.presentation.rovers.RoversRoute
import com.zooeydigital.marsrover.ui.theme.MarsRoverTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MarsRoverTheme {
                MarsRoverApp()
            }
        }
    }
}

sealed class Screen(val route: String) {
    object Rovers : Screen("rovers")

    object RoverDetail : Screen("rover_detail/{roverId}") {
        fun createRoute(roverId: String) = "rover_detail/$roverId"
    }
}

@Composable
fun MarsRoverApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBackButton = currentRoute != null && currentRoute != Screen.Rovers.route

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            NasaHeader(
                showBackButton = showBackButton,
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { contentPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            color = MaterialTheme.colorScheme.background,
        ) {
            NavHost(
                navController = navController,
                startDestination = Screen.Rovers.route,
            ) {
                composable(Screen.Rovers.route) {
                    RoversRoute(
                        onRoverClick = { rover ->
                            navController.navigate(Screen.RoverDetail.createRoute(rover.id))
                        }
                    )
                }
                composable(
                    route = Screen.RoverDetail.route,
                    arguments = listOf(
                        navArgument("roverId") { type = NavType.StringType }
                    )
                ) {
                    RoverDetailRoute()
                }
            }
        }
    }
}

@Composable
fun NasaHeader(
    showBackButton: Boolean,
    onBackClick: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 4.dp,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(64.dp)
                .padding(horizontal = 4.dp),
            contentAlignment = Alignment.Center,
        ) {
            if (showBackButton) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.go_back),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            Image(
                painter = painterResource(id = R.drawable.nasa_logo),
                contentDescription = stringResource(R.string.nasa_label),
                modifier = Modifier.height(36.dp),
                contentScale = ContentScale.Fit,
            )
        }
    }
}
