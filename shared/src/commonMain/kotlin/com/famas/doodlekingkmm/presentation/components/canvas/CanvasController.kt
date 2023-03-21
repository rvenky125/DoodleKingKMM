package com.famas.doodlekingkmm.presentation.components.canvas

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.*

class CanvasController {

    private val _redoPathList = mutableStateListOf<PathWrapper>()
    private val _undoPathList = mutableStateListOf<PathWrapper>()

    val pathList: SnapshotStateList<PathWrapper> = _undoPathList

    private val _historyTracker = MutableSharedFlow<History>(extraBufferCapacity = 1)
    val historyTracker = _historyTracker.asSharedFlow()


    var strokeWidth by mutableStateOf(10f)
        private set

    var color by mutableStateOf(Color.Black)
        private set

    var bgColor by mutableStateOf<Color?>(null)
        private set

    fun changeColor(value: Color) {
        color = value
    }

    fun changeBgColor(value: Color) {
        bgColor = value
    }

    fun changeStrokeWidth(value: Float) {
        strokeWidth = value
    }

    fun importPath(canvasData: CanvasData) {
        reset()
        bgColor = canvasData.bgColor
        _undoPathList.addAll(canvasData.path)
        _historyTracker.tryEmit(History(_undoPathList.size, _redoPathList.size))
    }

    fun exportPath() = CanvasData(bgColor, pathList.toList())


    fun unDo() {
        if (_undoPathList.isNotEmpty()) {
            val last = _undoPathList.last()
            _redoPathList.add(last)
            _undoPathList.remove(last)
            _historyTracker.tryEmit(History(_undoPathList.size, _redoPathList.size))
        }
    }

    fun reDo() {
        if (_redoPathList.isNotEmpty()) {
            val last = _redoPathList.last()
            _undoPathList.add(last)
            _redoPathList.remove(last)
            _historyTracker.tryEmit(History(_undoPathList.size, _redoPathList.size))
        }
    }


    fun reset() {
        _redoPathList.clear()
        _undoPathList.clear()
        _historyTracker.tryEmit(History())
    }

    fun updateLatestPath(newPoint: Offset) {
        val index = _undoPathList.lastIndex
        _undoPathList[index].points.add(newPoint)
    }

    fun insertNewPath(newPoint: Offset) {
        val pathWrapper = PathWrapper(
            points = mutableStateListOf(newPoint),
            strokeColor = color,
            strokeWidth = strokeWidth,
        )
        _undoPathList.add(pathWrapper)
        _redoPathList.clear()
        _historyTracker.tryEmit(History(_undoPathList.size, _redoPathList.size))
    }
}

@Composable
fun rememberCanvasController(): CanvasController {
    return remember { CanvasController() }
}

data class History(
    val undoCount: Int = 0,
    val redoCount: Int = 0
)