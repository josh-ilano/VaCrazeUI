package com.example.vacrazeui.ui.planner

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.widget.ConstraintSet.Transform
import androidx.navigation.NavController


private var TOTAL_TIME = 24 * 3600
private val HOUR_TIME = 60 * 60




@Composable
fun TimeBar() {

    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val state = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
        scale *= zoomChange
        offset += offsetChange
    }

    Box(
        modifier = Modifier.fillMaxWidth()
        .height(1000.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) // take up size of box's contents
        {
            val canvasWidth = size.width
            val canvasHeight = size.height


            // the gray bar used to track time
            drawRoundRect(
                cornerRadius = CornerRadius(16f, 16f),
                color = Color.LightGray,
                size = Size(canvasWidth, canvasHeight)
            )

            // Create the hourly intervals
            for (i in HOUR_TIME..TOTAL_TIME - HOUR_TIME step HOUR_TIME) {
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

        Box(
            Modifier
                .graphicsLayer(
                    scaleX=.5f,
                    scaleY = scale,
                    translationX = 0.25f,
                    translationY = offset.y
                )
                .transformable(state = state)
                .background(Color(0,255,0,150))
                .fillMaxSize()
        )  {
            Text("Time Block", fontSize = 30.sp)
        }
    }



}

@Composable
fun PlannerScreen(modifier: Modifier) {

    Row(modifier = Modifier.fillMaxSize()) {
        Column(modifier = modifier
            .weight(4f)
            .verticalScroll(rememberScrollState())) { // Time
            TimeBar()
        }
        Column(modifier = modifier.weight(6f),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Create Schedule", fontSize = 30.sp)
        }
    }

}
