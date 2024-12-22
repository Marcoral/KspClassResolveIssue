package com.example.greeter.implementation

import com.example.greeter.processor.Greet
import com.example.greeter.processor.Greeter
import com.google.devtools.ksp.processing.KSPLogger

@Greet("com.example.greeter.implementation.GreeterImpl")
object GreeterTest

object GreeterImpl : Greeter {
    override fun greet(logger: KSPLogger) {
        logger.error("Hello world!")
    }
}