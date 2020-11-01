import shared

typealias Dispatcher<State, Action> = (@escaping (State) -> Action?) -> Void

typealias StateObserver<State> = (@escaping (State?) -> Void) -> Void
