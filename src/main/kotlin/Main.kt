import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import kotlinx.coroutines.delay
import java.awt.MenuItem
import java.awt.PopupMenu

@Composable
fun Button(windowState: WindowState, buttonSize: Dp) {


    var currentColor by remember { mutableStateOf(Color.Cyan) }
    val animatedColor by animateColorAsState(targetValue = currentColor)

    var isWindowExpanded by remember { mutableStateOf(false) }


    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            currentColor = if (currentColor == Color.Gray) Color.Cyan else Color.Gray
        }
    }



    Button(
        onClick = {

            isWindowExpanded = !isWindowExpanded

            windowState.size =
                if (isWindowExpanded)
                    DpSize(330.dp, 874.dp)
                else
                    DpSize(buttonSize, buttonSize)
        },
//        colors = ButtonDefaults.buttonColors(animatedColor),
        modifier = Modifier.size(buttonSize)

    ) {
        Icon(
            Icons.Filled.Add,
            contentDescription = "Action",
            tint = Color.White,
        )
    }

}

fun main() = application {

    MaterialTheme {
        val painter = painterResource("./imt_tray_icon.png")
        val popupMenu = PopupMenu().apply {
            add(MenuItem("Exit").apply {
                addActionListener {
                    exitApplication()
                }
            })
        }
        var dragStartOffset by remember { mutableStateOf(Offset.Zero) }
        var windowStartPos by remember { mutableStateOf(WindowPosition(0.dp, 0.dp)) }
        Tray(
            icon = painter,
            tooltip = "Compose App",
        ) {

            Item("exit") { exitApplication() }

        }
        val buttonSize = 64.dp
        val windowPosition = WindowPosition(1000.dp, 20.dp)
        val windowState = rememberWindowState(
            width = buttonSize,
            height = buttonSize,
            position = windowPosition,
            isMinimized = false
        )
        var movedPosition = remember { mutableStateOf(WindowPosition(0.dp, 0.dp)) }
        Window(
            onCloseRequest = {},
            undecorated = true,
            alwaysOnTop = true,
            resizable = false,
            transparent = true,
            state = windowState
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = {
                                dragStartOffset = it
                                println("dragStartOffset X: ${it.x}")
                                println("dragStartOffset Y: ${it.y}")
                                windowStartPos = windowState.position as WindowPosition.Absolute

                            }
                        ) { change, dragAmount ->

                            val newPosition = WindowPosition(
                                x = windowState.position.x - dragStartOffset.x.dp + change.position.x.dp,
                                y = windowState.position.y - dragStartOffset.y.dp + change.position.y.dp
                            )
                            windowState.position = newPosition
                        }
                    }

            ) {
                Button(windowState, buttonSize)
                Spacer(Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .size(300.dp, 500.dp)
                        .shadow(elevation = 4.dp, shape = RoundedCornerShape(10.dp))
                        .background(Color.hsl(0f, 0f, 0.97f), shape = RoundedCornerShape(10.dp))

                )


            }
        }
    }
}
