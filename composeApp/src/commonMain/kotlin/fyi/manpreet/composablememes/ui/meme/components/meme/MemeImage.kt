package fyi.manpreet.composablememes.ui.meme.components.meme

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
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import fyi.manpreet.composablememes.data.model.Meme
import fyi.manpreet.composablememes.ui.meme.mapper.toFontFamily
import fyi.manpreet.composablememes.ui.meme.state.MemeEvent
import fyi.manpreet.composablememes.ui.meme.state.MemeTextBox
import fyi.manpreet.composablememes.ui.theme.fixedAccentColors
import fyi.manpreet.composablememes.ui.theme.spacing
import fyi.manpreet.composablememes.util.noRippleClickable
import fyi.manpreet.composablememes.util.toOffset
import fyi.manpreet.composablememes.util.toRelativePosition
import org.jetbrains.compose.resources.ExperimentalResourceApi
import kotlin.math.roundToInt

/**
 * A composable that displays a meme image with interactive, draggable text boxes positioned on top.
 * The text boxes stay within the image bounds regardless of screen orientation changes or container resizing.
 *
 * Core Concepts:
 *
 * 1. Image Scaling and Positioning
 * --------------------------------
 * ContentScale.Fit ensures:
 * - The entire image is visible without cropping
 * - Aspect ratio is preserved
 * - Image fits completely within container bounds
 * - Creates letterboxing (empty spaces) when container and image ratios differ
 *
 * Image positioning is calculated using aspect ratios:
 * - Image aspect ratio = original width / original height
 * - Container aspect ratio = container width / container height
 *
 * Two scaling scenarios:
 *
 * a) If image aspect ratio > container aspect ratio:
 *    - `Width` = container width
 *    - `Height` = container width / image aspect ratio
 *    - `X offset` = 0
 *    - `Y offset` = (container height - scaled height) / 2
 *
 * b) If image aspect ratio ≤ container aspect ratio:
 *    - `Width` = container height * image aspect ratio
 *    - `Height` = container height
 *    - `X offset` = (container width - scaled width) / 2
 *    - `Y offset` = 0
 *
 * 2. Text Box Positioning System
 * -----------------------------
 * Uses two coordinate systems:
 *
 * a) Absolute coordinates (Offset):
 *    - Actual pixel positions on screen
 *    - Accounts for letterboxing via imageContentOffset
 *    - Used for immediate rendering and drag operations
 *
 * b) Relative coordinates (RelativePosition):
 *    - Percentage-based positioning (0.0 to 1.0)
 *    - Stored as percentX and percentY
 *    - Orientation and size independent
 *
 * Conversion formulas:
 *
 * - Relative to Absolute:
 *   x = (percentX * imageWidth) + offsetX
 *   y = (percentY * imageHeight) + offsetY
 *
 * - Absolute to Relative:
 *   percentX = (x - offsetX) / imageWidth
 *   percentY = (y - offsetY) / imageHeight
 *
 * 3. State Management and Recomposition
 * -----------------------------------
 * Critical remember keys:
 * - `textBox.id`: Ensures state persistence per text box
 * - `imageContentSize`: Triggers recalculation on container resizing
 * - `imageContentOffset`: Updates positioning when letterboxing changes
 *
 * Local states with remember:
 * - `contentSize`: Tracks text box dimensions
 * - `localOffset`: Maintains current drag position
 *
 * 4. Drag Constraints
 * ------------------
 * Bounds checking ensures text boxes stay within image:
 * - `X boundaries`:
 *   min = imageContentOffset.x
 *   max = imageContentOffset.x + imageWidth - textBoxWidth
 *
 * - `Y boundaries`:
 *   min = imageContentOffset.y
 *   max = imageContentOffset.y + imageHeight - textBoxHeight
 *
 * 5. Touch Interaction System
 * --------------------------
 * Multiple input handlers:
 * - Single tap: Selects text box
 * - Double tap: Enables editing mode
 * - Drag: Updates position while maintaining bounds
 *
 * @param modifier Modifier for the root container
 * @param meme The meme data containing image information
 * @param textBoxes List of text boxes to display on the image
 * @param graphicsLayer Graphics layer for image effects
 * @param imageContentSize Actual size of the scaled image
 * @param imageContentOffset Offset of the image due to letterboxing
 * @param onPositionUpdate Callback for text box position updates
 * @param onTextBoxClick Callback for text box selection/editing
 * @param onTextBoxCloseClick Callback for text box deletion
 * @param onTextBoxTextChange Callback for text content updates
 * @param onDeselectClick Callback for deselection events
 * @param onEditorSizeUpdate Callback for container size updates
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
            AsyncImage(
                modifier = Modifier
                    .fillMaxSize()
                    .onGloballyPositioned { coordinates ->
                        val containerWidth = coordinates.size.width
                        val containerHeight = coordinates.size.height
                        val actualWidth: Float
                        val actualHeight: Float
                        val offsetX: Float
                        val offsetY: Float
                        val imageWidth = meme.width.toFloat()
                        val imageHeight = meme.height.toFloat()

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
                model = meme.path?.value,
                contentDescription = null,
                contentScale = ContentScale.Fit,
            )
        }

        Box(
            Modifier
                .size(imageContentSize.width.dp, imageContentSize.height.dp)
                .noRippleClickable { onDeselectClick(MemeEvent.EditorEvent.DeselectAllTextBox) }
        ) {

            textBoxes.forEach { textBox ->
                var contentSize by remember(textBox.id, imageContentSize, imageContentOffset) {
                    mutableStateOf(IntSize.Zero)
                }
                var localOffset by remember(textBox.id, imageContentSize, imageContentOffset) {
                    mutableStateOf(
                        textBox.relativePosition.toOffset(
                            size = imageContentSize,
                            contentOffset = imageContentOffset
                        )
                    )
                }

                Box(
                    modifier = Modifier
                        .offset {
                            IntOffset(
                                x = localOffset.x.roundToInt(),
                                y = localOffset.y.roundToInt()
                            )
                        }
                        .pointerInput(textBox.id, imageContentSize, imageContentOffset) {
                            detectTapGestures(
                                onTap = {
                                    onTextBoxClick(MemeEvent.EditorEvent.SelectTextBox(textBox.id))
                                },
                                onDoubleTap = {
                                    onTextBoxClick(MemeEvent.EditorEvent.EditTextBox(textBox.id))
                                }
                            )
                        }
                        .pointerInput(textBox.id, imageContentSize, imageContentOffset) {
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
                                    // Calculate relative position
                                    val relativePosition = localOffset.toRelativePosition(
                                        size = imageContentSize,
                                        contentOffset = imageContentOffset
                                    )
                                    onPositionUpdate(
                                        MemeEvent.EditorEvent.PositionUpdate(
                                            id = textBox.id,
                                            offset = localOffset,
                                            relativePosition = relativePosition
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
    onSizeChange: (IntSize) -> Unit,
) {

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    var textFieldValue by remember(textBox.id) {
        mutableStateOf(
            TextFieldValue(text = textBox.text, selection = TextRange(0, textBox.text.length))
        )
    }

    LaunchedEffect(textBox.id, textBox.isEditable) {
        if (textBox.isEditable) {
            focusRequester.requestFocus()
            keyboardController?.show()
        }
    }

    Box(
        modifier = modifier
            .then(if (textBox.isSelected) Modifier.border(1.dp, Color.White) else Modifier)
            .onGloballyPositioned { onSizeChange(it.size) }
    ) {

        // Keep this text field before the next text field to keep the stroke behind the actual text
        StrokedTextField(
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min)
                .padding(MaterialTheme.spacing.extraSmall),
            value = textFieldValue,
            textBox = textBox
        )

        // Main text field
        EditableTextField(
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min)
                .padding(MaterialTheme.spacing.extraSmall)
                .focusRequester(focusRequester),
            value = textFieldValue,
            onValueChange = {
                textFieldValue = it
                onTextBoxTextChange(
                    MemeEvent.EditorEvent.UpdateTextBox(text = it.text, selection = it.selection)
                )
            },
            textBox = textBox,
        )

        if (textBox.isSelected) {
            Icon(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 12.dp, y = (-12).dp)
                    .background(color = MaterialTheme.colorScheme.error, shape = CircleShape)
                    .clickable {
                        onTextBoxCloseClick(MemeEvent.EditorEvent.RemoveTextBox(textBox.id))
                    },
                imageVector = Icons.Default.Close,
                tint = Color.White,
                contentDescription = "Close"
            )
        }
    }
}

@Composable
fun StrokedTextField(
    modifier: Modifier = Modifier,
    value: TextFieldValue,
    textBox: MemeTextBox,
) {
    BasicTextField(
        value = value,
        onValueChange = {},
        modifier = modifier,
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
}

@Composable
private fun EditableTextField(
    modifier: Modifier = Modifier,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    textBox: MemeTextBox,
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = textBox.isEditable,
        textStyle = textBox.textStyle.copy(
            fontFamily = textBox.fontFamilyType.toFontFamily(),
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Default
        ),
        singleLine = false,
        cursorBrush = SolidColor(MaterialTheme.fixedAccentColors.secondaryFixedDim),
    )
}