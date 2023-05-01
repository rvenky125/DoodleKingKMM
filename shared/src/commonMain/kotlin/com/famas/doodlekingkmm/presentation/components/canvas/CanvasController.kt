package com.famas.doodlekingkmm.presentation.components.canvas

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CanvasController() {

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

    fun updateDrawDataManually(pathEvent: PathEvent) {
        when (pathEvent.type) {
            PathEvent.TYPE_UPDATE -> updateLatestPath(pathEvent.offset)
            PathEvent.TYPE_INSERT -> insertNewPath(pathEvent.offset)
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
    val offset: Offset,
    val type: Int
) {
    companion object {
        const val TYPE_INSERT = 0
        const val TYPE_UPDATE = 1
    }
}