import shared

typealias Dispatcher<State, Action> = (@escaping (State) -> Action?) -> Void

typealias StateObserver<State> = (@escaping (State?) -> Void) -> Void

struct AnyStore<Action, State> {

    let defaultState: State

    let stateObserver: StateObserver<State>

    let dispatch: Dispatcher<State, Action>

    let dispose: () -> Void

    fileprivate init(
        defaultState: State,
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

extension AnyStore {

    static func store<Result>(from viewModel: ViewModel<Action, Result, State>) -> Self {
        let mviController = viewModel.mviController

        return .init(
            defaultState: mviController.initialViewState,
            stateObserver: mviController.viewStatesFlow.watch,
            dispatch: mviController.accept,
            dispose: viewModel.close
        )
    }
}

extension AnyStore {

    static func preview(state: State) -> Self {
        self.init(
            defaultState: state,
            stateObserver: { _ in },
            dispatch: { _ in },
            dispose: { }
        )
    }
}
