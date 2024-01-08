package com.famas.doodlekingkmm.presentation.components.canvas

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class CanvasController() {
    val colors = listOf(
        Color.Black,
        Color.Green,
        Color.Red,
        Color.Blue,
        Color.Yellow
    )

    val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)

    private val _redoPathList = mutableStateListOf<PathWrapper>()
    private val _undoPathList = mutableStateListOf<PathWrapper>()

    val pathList: SnapshotStateList<PathWrapper> = _undoPathList

    private val pathEventsChannel = Channel<PathEvent>()
    val pathEventsFlow: Flow<PathEvent> = pathEventsChannel.receiveAsFlow()

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

    fun unDo(emitEvent: Boolean) {
        if (emitEvent) {
            coroutineScope.launch {
                pathEventsChannel.send(PathEvent(null, PathEvent.TYPE_UNDO))
            }
        }

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
        color = Color.Black
        _historyTracker.tryEmit(History())
    }

    fun updateDrawDataManually(pathEvent: PathEvent) {
        when (pathEvent.type) {
            PathEvent.TYPE_UPDATE -> pathEvent.offset?.let { updateLatestPath(it) }
            PathEvent.TYPE_INSERT -> pathEvent.offset?.let { insertNewPath(it) }
            PathEvent.TYPE_UNDO -> unDo(false)
        }
    }

    fun updateLatestPathFromUserInput(newPoint: Offset) {
        coroutineScope.launch {
            pathEventsChannel.send(PathEvent(newPoint, PathEvent.TYPE_UPDATE))
        }
        updateLatestPath(newPoint)
    }

    fun insertNewPathFromUserInput(newPoint: Offset) {
        coroutineScope.launch {
            pathEventsChannel.send(PathEvent(newPoint, PathEvent.TYPE_INSERT))
        }
        insertNewPath(newPoint)
    }

    private fun updateLatestPath(newPoint: Offset) {
        val index = _undoPathList.lastIndex
        _undoPathList[index].points.add(newPoint)
    }

    private fun insertNewPath(offset: Offset) {
        val pathWrapper = PathWrapper(
            points = mutableStateListOf(offset),
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

data class PathEvent(
    val offset: Offset? = null,
    val type: Int
) {
    companion object {
        const val TYPE_INSERT = 0
        const val TYPE_UPDATE = 1
        const val TYPE_UNDO = 2
    }
}