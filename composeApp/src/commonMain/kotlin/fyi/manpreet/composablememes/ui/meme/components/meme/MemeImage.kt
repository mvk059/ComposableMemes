package fyi.manpreet.composablememes.ui.meme.components.meme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import composablememes.composeapp.generated.resources.Res
import composablememes.composeapp.generated.resources.allDrawableResources
import fyi.manpreet.composablememes.data.model.Meme
import fyi.manpreet.composablememes.ui.meme.state.MemeEvent
import fyi.manpreet.composablememes.ui.meme.state.MemeTextBox
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import kotlin.math.roundToInt

@OptIn(ExperimentalResourceApi::class)
@Composable
fun MemeImage(
    modifier: Modifier = Modifier,
    meme: Meme,
    textBoxes: List<MemeTextBox>,
    onPositionUpdate: (MemeEvent.EditorEvent) -> Unit,
    onTextBoxClick: (MemeEvent.EditorEvent) -> Unit,
    onTextBoxCloseClick: (MemeEvent.EditorEvent) -> Unit,
    onDeselectClick: (MemeEvent.EditorEvent) -> Unit,
) {

    val image: DrawableResource = Res.allDrawableResources[meme.imageName] ?: return
    var size by remember { mutableStateOf(IntSize.Zero) }

    Image(
        modifier = modifier.onGloballyPositioned { size = it.size },
        painter = painterResource(image),
        contentDescription = null,
        contentScale = ContentScale.Fit,
    )

    Box(
        Modifier
            .size(size.width.dp, size.height.dp)
            .clickable { onDeselectClick(MemeEvent.EditorEvent.DeselectTextBox) }
    ) {

        textBoxes.forEach { textBox ->
            var contentSize by remember { mutableStateOf(IntSize.Zero) }
            var localOffset by remember(textBox.id) { mutableStateOf(textBox.offset) }

            Box(
                modifier = Modifier
                    .offset {
                        IntOffset(x = localOffset.x.roundToInt(), y = localOffset.y.roundToInt())
                    }
                    .pointerInput(textBox.id) {
                        detectTapGestures(
                            onTap = {
                                onTextBoxClick(MemeEvent.EditorEvent.SelectTextBox(textBox.id))
                            }
                        )
                    }
                    .pointerInput(textBox.id) {
                        detectDragGestures(
                            onDrag = { _, dragAmount ->
                                val original = localOffset
                                val summed = original + dragAmount

                                val newValue = Offset(
                                    x = summed.x.coerceIn(0f, size.width.toFloat() - contentSize.width.toFloat()),
                                    y = summed.y.coerceIn(0f, size.height.toFloat() - contentSize.height.toFloat())
                                )

                                localOffset = newValue
                            },
                            onDragEnd = {
                                onPositionUpdate(MemeEvent.EditorEvent.PositionUpdate(textBox.id, localOffset))
                            }
                        )
                    }
            ) {

                Content(
                    textBox = textBox,
                    onTextBoxCloseClick = onTextBoxCloseClick,
                    onSizeChange = { contentSize = it }
                )
            }
        }
    }
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    textBox: MemeTextBox,
    onTextBoxCloseClick: (MemeEvent.EditorEvent) -> Unit,
    onSizeChange: (IntSize) -> Unit,
) {

    Box(
        modifier = modifier
            .wrapContentSize()
            .then(
                if (textBox.isSelected) Modifier.border(1.dp, Color.White)
                else Modifier
            )
            .onGloballyPositioned { onSizeChange(it.size) }
            .background(Color.Green)
    ) {

        Text(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            text = textBox.text,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White,
            textAlign = TextAlign.Center
        )

        if (textBox.isSelected) {
            Icon(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 12.dp, y = (-12).dp)
                    .background(color = MaterialTheme.colorScheme.error, shape = CircleShape)
                    .clickable {
                        println("TextBox Close Clicked: $textBox")
                        onTextBoxCloseClick(MemeEvent.EditorEvent.RemoveTextBox(textBox.id))
                    },
                imageVector = Icons.Default.Close,
                tint = Color.White,
                contentDescription = "Close"
            )
        }
    }
}