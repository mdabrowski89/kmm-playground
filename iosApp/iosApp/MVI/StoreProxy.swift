import shared

typealias Dispatcher<State, Action> = (@escaping (State) -> Action?) -> Void

typealias StateObserver<State> = (@escaping (State?) -> Void) -> Void

struct StoreProxy<Action, State> {

    let defaultState: () -> State

    let stateObserver: StateObserver<State>

    let dispatch: Dispatcher<State, Action>

    let dispose: () -> Void

    fileprivate init(
        defaultState: @escaping () -> State,
        stateObserver: @escaping StateObserver<State>,
        dispatch: @escaping Dispatcher<State, Action>,
        dispose: @escaping () -> Void
    ) {
        self.defaultState = defaultState
        self.stateObserver = stateObserver
        self.dispatch = dispatch
        self.dispose = dispose
    }
}

extension StoreProxy {

    init<Result>(
        _ mviController: MviController<Action, Result, State>,
        dispose: @escaping () -> Void
    ) {
        self.init(
            defaultState: mviController.defaultViewState,
            stateObserver: mviController.viewStatesFlow.watch,
            dispatch: mviController.accept,
            dispose: dispose
        )
    }
}

extension StoreProxy {

    static func preview(state: State) -> Self {
        self.init(
            defaultState: { state },
            stateObserver: { _ in },
            dispatch: { _ in },
            dispose: { }
        )
    }
}
