package dev.celestialfault.commander

import dev.celestialfault.commander.annotations.Greedy
import net.minecraft.command.CommandSource
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.KType
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.jvm.jvmErasure

internal fun KType.starProjected() =
	classifier?.starProjectedType ?: throw UnsupportedOperationException("Cannot star project type $this")

// Types like 'String?' break type lookups if we don't do this star projection
internal fun <S : CommandSource> Map<KType, ArgumentHandler<*, S>>.getHandler(param: KParameter) =
	this[param.type.starProjected()] ?: error("Unknown type ${param.type}")

internal fun parameterSanityCheck(parameters: Collection<KParameter>) {
	var foundOptional = false
	var foundGreedy = false
	parameters.forEach {
		if(it.isOptional) {
			foundOptional = true
		} else {
			check(!foundOptional) { "Required parameters may not follow an optional parameter" }
		}

		check(!foundGreedy) { "No extra parameters may follow a @Greedy argument" }
		if(it.hasAnnotation<Greedy>()) foundGreedy = true
	}
}

internal fun KType.isSubclassOf(cls: KClass<*>) = jvmErasure.isSubclassOf(cls)
