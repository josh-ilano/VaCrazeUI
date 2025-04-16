package com.example.vacrazeui.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.navigation.compose.composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.vector.ImageVector

import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*

import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

import com.example.vacrazeui.ui.planner.PlannerScreen
import com.example.vacrazeui.ui.favorite.FavoriteScreen
import com.example.vacrazeui.ui.account.AccountScreen
import com.example.vacrazeui.ui.map.MapScreen

enum class VacrazeScreen() {
    Home,
    Add,
    Favorite,
    Map,
    Account
}

//class Time {
//    companion object {
//        val TOTAL_TIME = 24 * 3600
//        val HOUR_TIME = 60 * 60
//    }
//}

private var TOTAL_TIME = 24 * 3600
private val HOUR_TIME = 60 * 60

@Composable
fun Label(text: String, icon: ImageVector) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text, modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )
        FloatingActionButton(
            modifier = Modifier.weight(1f),
            onClick = { },
        ) { Icon(icon, text) }
    }

}

@Composable
fun ButtonLayout(navController: NavHostController) {
    BottomAppBar() {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            FloatingActionButton(
                onClick = { navController.navigate(VacrazeScreen.Add.name) },
                modifier = Modifier.padding(horizontal = 10.dp)
            ) { Icon(Icons.Filled.Add, "Add") }
            Spacer(Modifier.height(16.dp))
            FloatingActionButton(
                onClick = { navController.navigate(VacrazeScreen.Favorite.name) },
                modifier = Modifier.padding(horizontal = 10.dp)
            ) { Icon(Icons.Filled.Star, "Star") }
            Spacer(Modifier.height(16.dp))
            FloatingActionButton(
                onClick = { navController.navigate(VacrazeScreen.Home.name) },
                modifier = Modifier.padding(horizontal = 10.dp)
            ) { Icon(Icons.Filled.Home, "Home") }
            Spacer(Modifier.height(16.dp))
            FloatingActionButton(
                onClick = { navController.navigate(VacrazeScreen.Account.name) },
                modifier = Modifier.padding(horizontal = 10.dp)
            ) { Icon(Icons.Filled.Person, "Account") }
            Spacer(Modifier.height(16.dp))
            FloatingActionButton(
                onClick = { navController.navigate(VacrazeScreen.Map.name) },
                modifier = Modifier.padding(horizontal = 10.dp)
            ) { Icon(Icons.Filled.LocationOn, "Explore Map") }
        }
    }
}




@Composable
fun TimeBar(totalSeconds: Int) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(1000.dp)
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        // the gray bar used to track time
        drawRoundRect(
            cornerRadius = CornerRadius(16f, 16f),
            color = Color.LightGray,
            size = Size(canvasWidth, canvasHeight)
        )

        // dashed line that represents what time it is
        val timeLeft = canvasHeight - ((totalSeconds / TOTAL_TIME.toFloat()) * canvasHeight)
        drawLine(
            strokeWidth = 5f,
            start = Offset(x = canvasWidth - 30, y = timeLeft),
            end = Offset(x = canvasWidth + 75, y = timeLeft),
            color = Color.Black
        )

        // Create the hourly intervals
        for (i in HOUR_TIME..TOTAL_TIME-HOUR_TIME step HOUR_TIME) {
            val yPosition = canvasHeight - ((i / TOTAL_TIME.toFloat()) * canvasHeight)
            drawLine(
                strokeWidth = 5f,
                start = Offset(x = 0f, y = yPosition),
                end = Offset(x = canvasWidth, y = yPosition),
                color = Color(0,0,0,50),
                pathEffect = if (i!= HOUR_TIME * 12)  PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f) else null
            )
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(modifier: Modifier = Modifier) {

    val coroutineScope = rememberCoroutineScope()

    var currentTime by remember { mutableStateOf(LocalDateTime.now()) }
    var totalSeconds by remember { mutableIntStateOf(0) }

    val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
    val formatted = currentTime.format(formatter)


    LaunchedEffect(Unit) { // keep track of time
        coroutineScope.launch(Dispatchers.IO) {
            while (true) {
                currentTime = LocalDateTime.now()
                totalSeconds = (currentTime.hour * 3600) + (currentTime.minute * 60)
                delay(1000 * 30) // update every 30 seconds
            }
        }
    }

    Row(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier
                .weight(4f)
                .verticalScroll(rememberScrollState())) { // Time
            TimeBar(totalSeconds)
        }

        val array = formatted.split(", ")
        val date = array[0]; val time = array[1]
        Column(modifier = modifier.weight(6f),
            horizontalAlignment = Alignment.CenterHorizontally) {

            Text(text = date, fontSize = 30.sp)
            Text(text = time, fontSize = 20.sp)
        }

    }


}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun App(
    navController: NavHostController = rememberNavController()
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { ButtonLayout(navController) }
    ) { innerPadding ->
        NavHost( // what allows us to navigate
            navController = navController,
            startDestination = VacrazeScreen.Home.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = VacrazeScreen.Home.name) { HomeScreen(modifier = Modifier.padding(innerPadding)) }
            composable(route = VacrazeScreen.Add.name) { PlannerScreen(modifier = Modifier.padding(innerPadding)) }
            composable(route = VacrazeScreen.Favorite.name) { FavoriteScreen(modifier = Modifier.padding(innerPadding)) }
            composable(route = VacrazeScreen.Account.name) { AccountScreen(modifier = Modifier.padding(innerPadding)) }
            composable(route = VacrazeScreen.Map.name) { MapScreen(modifier = Modifier.padding(innerPadding)) }
        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun DisplayHome() {
    HomeScreen(modifier=Modifier)
}
