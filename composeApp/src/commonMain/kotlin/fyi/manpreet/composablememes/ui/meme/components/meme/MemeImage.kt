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
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.layer.drawLayer
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

/**
 * Composable that displays the meme image and text boxes on top of it.
 * The text boxes fits entirely inside the bounds of the image
 *
 * Logic of fitting the text boxes inside the image bounds:
 *
 * `ContentScale.Fit` - ContentScale.Fit is a scaling method that ensures:
 * * The entire image is visible
 * * No part of the image is cropped
 * * Aspect ratio is maintained
 * * The image fits completely within the container
 *
 * When ContentScale.Fit is used, the image will:
 * * Scale to fit entirely within the container
 * * Maintain its original proportions
 * * Potentially create empty spaces (letterboxing) if the container's shape differs from the image's
 *
 * `OnGloballyPositioned calculation` - The onGloballyPositioned modifier is used to calculate the size and position of the image.
 *
 * Aspect Ratio - An aspect ratio is the proportional relationship between an image's width and height.
 *
 * If the aspect ratio of the original image without scaling is greater than the aspect ratio of the parent composable,
 * the image will be scaled to fit the width of the parent composable and the height will be calculated based on the aspect ratio of the image.
 * * scaled width: width of the parent composable
 * * scaled height: width of the parent composable divided by the aspect ratio of the image
 * * offsetX: 0 as the entire width of the image is visible
 * * offsetY: Subtract the scaled image width from the parent composable height and divide by 2 to center the image horizontally
 *
 * If the aspect ratio of the original image without scaling is less than the aspect ratio of the parent composable,
 * the image will be scaled to fit the height of the parent composable and the width will be calculated based on the aspect ratio of the image.
 *
 *
 */
@OptIn(ExperimentalResourceApi::class)
@Composable
fun MemeImage(
    modifier: Modifier = Modifier,
    meme: Meme,
    textBoxes: List<MemeTextBox>,
    graphicsLayer: GraphicsLayer,
    imageContentSize: Size,
    imageContentOffset: Offset,
    onPositionUpdate: (MemeEvent.EditorEvent) -> Unit,
    onTextBoxClick: (MemeEvent.EditorEvent) -> Unit,
    onTextBoxCloseClick: (MemeEvent.EditorEvent) -> Unit,
    onTextBoxTextChange: (MemeEvent.EditorEvent) -> Unit,
    onDeselectClick: (MemeEvent.EditorEvent) -> Unit,
    onEditorSizeUpdate: (MemeEvent.EditorEvent) -> Unit,
) {

    val image: DrawableResource = Res.allDrawableResources[meme.imageName.value] ?: return
    val painter = painterResource(image)

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = MaterialTheme.spacing.bottomBarGapSize)
            .drawWithCache {
                onDrawWithContent {
                    graphicsLayer.record {
                        this@onDrawWithContent.drawContent()
                    }
                    drawLayer(graphicsLayer)
                }
            }
    ) {

        Box {
            Image(
                modifier = Modifier
                    .fillMaxSize()
                    .onGloballyPositioned { coordinates ->
                        val containerWidth = coordinates.size.width
                        val containerHeight = coordinates.size.height
                        val actualWidth: Float
                        val actualHeight: Float
                        val offsetX: Float
                        val offsetY: Float
                        val imageWidth = painter.intrinsicSize.width
                        val imageHeight = painter.intrinsicSize.height

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

                        val imageSize = Size(actualWidth, actualHeight)
                        val imageOffset = Offset(offsetX, offsetY)
                        onEditorSizeUpdate(
                            MemeEvent.EditorEvent.EditorSize(
                                editorSize = coordinates.size,
                                imageSize = imageSize,
                                imageOffset = imageOffset
                            )
                        )
                    },
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.Fit,
            )
        }

        Box(
            Modifier
                .size(imageContentSize.width.dp, imageContentSize.height.dp)
                .clickable { onDeselectClick(MemeEvent.EditorEvent.DeselectAllTextBox) }
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
                                            imageContentSize.width + imageContentOffset.x - contentSize.width.toFloat()
                                        ),
                                        y = summed.y.coerceIn(
                                            imageContentOffset.y,
                                            imageContentSize.height + imageContentOffset.y - contentSize.height.toFloat()
                                        )
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
                        onTextBoxTextChange = onTextBoxTextChange,
                        onTextBoxCloseClick = onTextBoxCloseClick,
                        onDeselectClick = onDeselectClick,
                        onSizeChange = { contentSize = it }
                    )
                }
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
                .padding(MaterialTheme.spacing.extraSmall),
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
                .padding(MaterialTheme.spacing.extraSmall)
                .focusRequester(focusRequester),
            enabled = textBox.isEditable,
            textStyle = textBox.textStyle.copy(
                fontFamily = textBox.fontFamilyType.toFontFamily(),
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Default
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
