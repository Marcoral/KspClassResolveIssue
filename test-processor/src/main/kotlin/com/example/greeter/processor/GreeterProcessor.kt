package com.example.greeter.processor

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.google.devtools.ksp.validate

annotation class Greet(val greeterName: String)

interface Greeter {
    fun greet(logger: KSPLogger)
}

class GreeterProcessor(
    val logger: KSPLogger
) : SymbolProcessor {
    override fun process(resolver: Resolver) = GreeterProcessorContext(logger, resolver).process()

    private class GreeterProcessorContext(val logger: KSPLogger,
                                          val resolver: Resolver) {
        fun process(): List<KSAnnotated> {
            val symbols = resolver.getSymbolsWithAnnotation(Greet::class.qualifiedName!!)
            val ret = symbols.filter { !it.validate() }.toList()
            symbols
                .filter { it is KSClassDeclaration && it.validate() }
                .forEach { it.accept(GreeterVisitor(), Unit) }
            return ret
        }

        inner class GreeterVisitor : KSVisitorVoid() {
            @OptIn(KspExperimental::class)
            override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
                val greeterName = classDeclaration.getAnnotationsByType(Greet::class).first().greeterName
                val correspondingGreeterClassDeclaration = resolver.getClassDeclarationByName(resolver.getKSNameFromString(greeterName))
                if (correspondingGreeterClassDeclaration == null) {
                    logger.error("No greeter of name $greeterName for class ${classDeclaration.qualifiedName}", classDeclaration)
                    return
                }
                val classDeclarationName = classDeclaration.simpleName.asString()
                logger.info("Corresponding greeter class declaration for $classDeclarationName: ${correspondingGreeterClassDeclaration.simpleName.asString()} - using getClassDeclarationByName")   //This works fine
                val greeterClass = Class.forName(greeterName)    //Throws java.lang.ClassNotFoundException
                logger.info("Corresponding greeter class declaration for $classDeclarationName: ${greeterClass.canonicalName} - using Class.forName()")
                val greeterInstance = greeterClass.kotlin.objectInstance!! as Greeter
                greeterInstance.greet(logger)
            }
        }
    }
}

class GreeterProcessorProvider : SymbolProcessorProvider {
    override fun create(
        environment: SymbolProcessorEnvironment
    ): SymbolProcessor {
        return GreeterProcessor(environment.logger)
    }
}