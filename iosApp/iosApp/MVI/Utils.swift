import SwiftUI
import shared

typealias CoroutineScopeFactory = CoroutineScopesKt
typealias CoroutineScopeType = Kotlinx_coroutines_coreCoroutineScope

typealias Store<Action, Result, State> = MviController<Action, Result, State>
    where Action: AnyObject, Result: AnyObject, State: AnyObject

typealias TestStore<Action, Result, State> = TestMviController<Action, Result, State>
    where Action: AnyObject, Result: AnyObject, State: AnyObject
