package pl.mobite.playground.domain.home.mvi

import pl.mobite.playground.common.mvi.MviControllerProvider
import pl.mobite.playground.common.mvi.processing.MviActionProcessing
import pl.mobite.playground.common.mvi.processing.MviResultProcessingProvider
import pl.mobite.playground.domain.home.mvi.impl.*
import pl.mobite.playground.domain.home.mvi.impl.HomeResult.InternalResults

/**
 * This is a kind of boilerplate code which is written in order to provide a strong types for all
 * mvi related classes from one feature. It is done in order to simplify dependency injection which
 * in case of Koin relies on class types.
 *
 * An alternative solution is to use Koin named() qualifiers, in example:
 * ```
 * factory(named("HomeActionProcessor")) {
 *     HomeActionProcessor(...)
 * }
 *
 * factory(named("HomeActionProcessingFLow")) {
 *     MviActionProcessingFlow<MviAction, MviResult>(
 *         mviActionProcessor = get(named("HomeActionProcessor"))
 *     )
 * }
 *
 * factory(named("HomeMviController")) { (...) ->
 *     MviController<MviAction, MviResult, MviViewState>(
 *         mviActionProcessingFlow = get(named("HomeActionProcessingFLow")),
 *         ...
 *     )
 * }
 * ```
 * However (in my opinion) providing strong type is less error prone then named() qualifiers.
 */

class HomeControllerProvider(
    homeActionProcessing: HomeActionProcessing,
    homeResultProcessingProvider: HomeResultProcessingProvider,
) : MviControllerProvider<HomeAction, HomeResult, HomeViewState>(
    mviActionProcessing = homeActionProcessing,
    mviResultProcessingProvider = homeResultProcessingProvider,
)

/**
 * HomeActionProcessingProvider which delegates processing of each action to "specialized" processor.
 * This is only the conventions and it could be implemented differently.
 *
 * "specialized" processors are extracted to separate classes because
 * each of them has different dependencies and it is responsible for processing different action.
 *
 */
class HomeActionProcessing(
    private val loadTasksActionProcessor: LoadTasksActionProcessor,
    private val addTaskActionProcessor: AddTaskActionProcessor,
    private val updateTaskActionProcessor: UpdateTaskActionProcessor,
    private val deleteCompletedTasksActionProcessor: DeleteCompletedTasksActionProcessor
) : MviActionProcessing<HomeAction, HomeResult>() {

    override fun processing(action: HomeAction) = when (action) {
        is HomeAction.LoadTasksAction -> loadTasksActionProcessor(action)
        is HomeAction.AddTaskAction -> addTaskActionProcessor(action)
        is HomeAction.UpdateTaskAction -> updateTaskActionProcessor(action)
        is HomeAction.DeleteCompletedTasksAction -> deleteCompletedTasksActionProcessor(action)
    }

    /**
     * We can omit implementation of this function if no action looping is necessary
     * */
    override suspend fun postProcessing(result: HomeResult): HomeResult? {
        when (result as? InternalResults) {
            InternalResults.ReloadResult -> accept(HomeAction.LoadTasksAction)
        }

        return result.takeUnless { it is InternalResults }
    }
}

class HomeResultProcessingProvider(
    homeResultReducer: HomeResultReducer,
) : MviResultProcessingProvider<HomeResult, HomeViewState>(
    mviResultReducer = homeResultReducer
)

