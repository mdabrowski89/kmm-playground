import SwiftUI
import Combine
import shared

@dynamicMemberLookup
final class ViewStore<Action, State>: ObservableObject {

    private(set) var state: State {
        willSet {
            objectWillChange.send()
        }
    }

    private let _dispatch: (@escaping (State) -> Action?) -> Void

    private let dispose: () -> Void

    private var viewCancellable: AnyCancellable?

    init(
        store: Store<Action, State>,
        removeDuplicates isDuplicate: @escaping (State, State) -> Bool
    ) where Action: AnyObject, State: AnyObject {
        self.state = store.initialState
        self._dispatch = store.dispatch
        self.dispose = store.dispose
        self.viewCancellable = StatePublisher(store.stateObserver)
            .removeDuplicates(by: isDuplicate)
            .sink { [weak self] in self?.state = $0 }
    }

    deinit {
        dispose()
    }

    subscript<LocalState>(dynamicMember keyPath: KeyPath<State, LocalState>) -> LocalState {
        state[keyPath: keyPath]
    }

    func dispatch(_ intent: @escaping (State) -> Action?) {
        _dispatch(intent)
    }
}

extension ViewStore where State: Equatable {

    convenience init(
        store: Store<Action, State>
    ) where Action: AnyObject, State: AnyObject {
        self.init(
            store: store,
            removeDuplicates: ==
        )
    }
}
