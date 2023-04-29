package com.famas.doodlekingkmm.core.util
import platform.Foundation.NSUUID

actual fun randomUUID(): String = NSUUID().UUIDString()

