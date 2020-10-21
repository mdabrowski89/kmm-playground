import shared

typealias Store<Action, Result, State> = MviController<Action, Result, State>
    where Action: AnyObject, Result: AnyObject, State: AnyObject
