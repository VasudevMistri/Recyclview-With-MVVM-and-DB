package com.app.wamatask.networks

import java.io.IOException

class NoConnectivityException : IOException() {
  override  val message: String
        get() = "No connectivity exception"
}