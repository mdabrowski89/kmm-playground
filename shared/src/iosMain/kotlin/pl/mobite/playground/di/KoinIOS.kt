package pl.mobite.playground.di

import kotlinx.cinterop.ObjCClass
import kotlinx.cinterop.getOriginalKotlinClass
import org.koin.core.Koin
import org.koin.core.definition.Definition
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.Qualifier
import org.koin.dsl.module
import pl.mobite.playground.data.service.TaskService

fun doInitKoin(taskService: Definition<TaskService>) = doInitKoin(
    appDeclaration = {
        modules(
            module {
                single(definition = taskService)
            }
        )
    }
)

fun Koin.get(objCClass: ObjCClass, qualifier: Qualifier?, parameter: Any): Any {
    val kClazz = getOriginalKotlinClass(objCClass)!!
    return get(kClazz, qualifier) { parametersOf(parameter) }
}

fun Koin.get(objCClass: ObjCClass, parameter: Any): Any {
    val kClazz = getOriginalKotlinClass(objCClass)!!
    return get(kClazz, null) { parametersOf(parameter) }
}

fun Koin.get(objCClass: ObjCClass, qualifier: Qualifier?): Any {
    val kClazz = getOriginalKotlinClass(objCClass)!!
    return get(kClazz, qualifier, null)
}

fun Koin.get(objCClass: ObjCClass): Any {
    val kClazz = getOriginalKotlinClass(objCClass)!!
    return get(kClazz, null, null)
}