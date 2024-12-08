package fyi.manpreet.composablememes.ui.meme.components.meme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import composablememes.composeapp.generated.resources.Res
import composablememes.composeapp.generated.resources.allDrawableResources
import fyi.manpreet.composablememes.data.model.Meme
import fyi.manpreet.composablememes.ui.meme.mapper.toFontFamily
import fyi.manpreet.composablememes.ui.meme.state.MemeEvent
import fyi.manpreet.composablememes.ui.meme.state.MemeTextBox
import fyi.manpreet.composablememes.ui.theme.fixedAccentColors
import fyi.manpreet.composablememes.ui.theme.spacing
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
    onTextBoxTextChange: (MemeEvent.EditorEvent) -> Unit,
    onDeselectClick: (MemeEvent.EditorEvent) -> Unit,
) {

    val image: DrawableResource = Res.allDrawableResources[meme.imageName] ?: return
    var imageContentSize by remember { mutableStateOf(Size.Zero) }
    var imageContentOffset by remember { mutableStateOf(Offset.Zero) }

    val painter = painterResource(image)
    val imageWidth = painter.intrinsicSize.width
    val imageHeight = painter.intrinsicSize.height

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = MaterialTheme.spacing.bottomBarGapSize)
    ) {
        Image(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer { clip = true }
                .onGloballyPositioned { coordinates ->
                    val containerWidth = coordinates.size.width
                    val containerHeight = coordinates.size.height
                    val actualWidth: Float
                    val actualHeight: Float
                    val offsetX: Float
                    val offsetY: Float

                    //  // Calculate aspect ratios
                    val imageAspectRatio = imageWidth / imageHeight
                    val containerAspectRatio = containerWidth.toFloat() / containerHeight

                    if (imageAspectRatio > containerAspectRatio) {
                        // Scale to container width, reduce height proportionally
                        actualWidth = containerWidth.toFloat()
                        actualHeight = containerWidth / imageAspectRatio
                        offsetX = 0f
                        offsetY = (containerHeight - actualHeight) / 2
                    } else {
                        // Scale to container height, reduce width proportionally
                        actualWidth = containerHeight * imageAspectRatio
                        actualHeight = containerHeight.toFloat()
                        offsetX = (containerWidth - actualWidth) / 2
                        offsetY = 0f
                    }

                    imageContentSize = Size(actualWidth, actualHeight)
                    imageContentOffset = Offset(offsetX, offsetY)
                },
            painter = painter,
            contentDescription = null,
            contentScale = ContentScale.Fit,
        )
    }

    Box(
        Modifier
            .size(imageContentSize.width.dp, imageContentSize.height.dp)
//            .clickable { onDeselectClick(MemeEvent.EditorEvent.DeselectTextBox) }
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
                            },
                            onDoubleTap = {
                                onTextBoxClick(MemeEvent.EditorEvent.EditTextBox(textBox.id))
                            }
                        )
                    }
                    .pointerInput(textBox.id) {
                        detectDragGestures(
                            onDrag = { _, dragAmount ->
                                val original = localOffset
                                val summed = original + dragAmount

                                val newValue = Offset(
                                    x = summed.x.coerceIn(
                                        imageContentOffset.x,
                                        imageContentSize.width.toFloat() + imageContentOffset.x - contentSize.width.toFloat()// - 12.dp.toPx()
                                    ),
                                    y = summed.y.coerceIn(
                                        imageContentOffset.y,
                                        imageContentSize.height.toFloat() + imageContentOffset.y - contentSize.height.toFloat()// - 12.dp.toPx()
                                    )
                                )

                                localOffset = newValue
                            },
                            onDragEnd = {
                                onPositionUpdate(
                                    MemeEvent.EditorEvent.PositionUpdate(
                                        textBox.id,
                                        localOffset
                                    )
                                )
                            }
                        )
                    }
            ) {

                Content(
                    textBox = textBox,
                    onTextBoxTextChange = onTextBoxTextChange,
                    onTextBoxCloseClick = onTextBoxCloseClick,
                    onDeselectClick = onDeselectClick,
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
    onTextBoxTextChange: (MemeEvent.EditorEvent) -> Unit,
    onTextBoxCloseClick: (MemeEvent.EditorEvent) -> Unit,
    onDeselectClick: (MemeEvent.EditorEvent) -> Unit,
    onSizeChange: (IntSize) -> Unit,
) {

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(textBox.isEditable) {
        if (textBox.isEditable) {
            focusRequester.requestFocus()
            keyboardController?.show()
        }
    }

    Box(
        modifier = modifier
//            .wrapContentSize()
            .then(
                if (textBox.isSelected) Modifier.border(1.dp, Color.White)
                else Modifier
            )
            .onGloballyPositioned {
                onSizeChange(it.size)
            }
    ) {

        // Keep this text field before the next text field to keep the stroke behind the actual text
        BasicTextField(
            value = TextFieldValue(text = textBox.text),
            onValueChange = {},
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min)
                .padding(MaterialTheme.spacing.small)
                .imePadding(),
            enabled = false,
            readOnly = true,
            textStyle = TextStyle.Default.copy(
                fontFamily = textBox.fontFamilyType.toFontFamily(),
                fontSize = textBox.textStyle.fontSize,
                color = Color.Black,
                drawStyle = Stroke(
                    miter = 5f,
                    width = 5f,
                    join = StrokeJoin.Round,
                ),
            )
        )

        BasicTextField(
            value = TextFieldValue(text = textBox.text, selection = TextRange(textBox.text.length)),
            onValueChange = { onTextBoxTextChange(MemeEvent.EditorEvent.UpdateTextBox(it.text)) },
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min)
                .padding(MaterialTheme.spacing.small)
                .imePadding()
                .focusRequester(focusRequester),
            enabled = textBox.isEditable,
            textStyle = textBox.textStyle.copy(
                fontFamily = textBox.fontFamilyType.toFontFamily(),
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    onDeselectClick(
                        MemeEvent.EditorEvent.DeselectTextBox(
                            id = textBox.id,
                            isSelected = true,
                            isEditable = false
                        )
                    )
                }
            ),
            singleLine = false,
            cursorBrush = SolidColor(MaterialTheme.fixedAccentColors.secondaryFixedDim),
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
