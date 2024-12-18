package fyi.manpreet.composablememes.usecase

class HistoryUseCase<T>(private val maxSize: Int = 5) {

    private val undoStack = ArrayDeque<T>()
    private val redoStack = ArrayDeque<T>()

    private var currentState: T? = null

    fun recordState(newState: T) {
        undoStack.addLast(newState)
        if (isUndoStackFull()) undoStack.removeFirst()
        this.currentState = newState
        redoStack.clear()
    }

    fun undo(): T? {
        val previousState = undoStack.removeLastOrNull() ?: return null
        redoStack.addLast(previousState)
        currentState = previousState
        return undoStack.lastOrNull()
    }

    fun redo(): T? {
        val nextState = redoStack.removeLastOrNull() ?: return null
        undoStack.addLast(nextState)
        currentState = nextState
        return nextState
    }

    fun canUndo(): Boolean = undoStack.isNotEmpty()

    fun canRedo(): Boolean = redoStack.isNotEmpty()

    fun clear() {
        undoStack.clear()
        redoStack.clear()
        currentState = null
    }

    private fun isUndoStackFull() = undoStack.size == maxSize
}
