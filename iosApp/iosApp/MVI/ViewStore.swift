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

    private let dispatch: Dispatcher<State, Action>

    private let dispose: () -> Void

    private var viewCancellable: AnyCancellable?

    private var bindings: [AnyHashable: String] = [:]

    init(
        store: AnyStore<Action, State>,
        removeDuplicates isDuplicate: @escaping (State, State) -> Bool
    ) {
        self.state = store.defaultState()
        self.dispatch = store.dispatch
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

    func accept(_ intent: @escaping (State) -> Action?) {
        dispatch(intent)
    }

    func binding<Value>(
        for keyPath: KeyPath<State, MviEventRaw<Value>?>,
        id: AnyHashable
    ) -> EventBinding<Value> {
        guard let event = state[keyPath: keyPath] else {
            return .constant(nil)
        }

        return .init(
            get: { self.bindings[id] == event.id ? nil : event },
            set: {
                guard $0 == nil else { return }
                self.bindings[id] = event.id
            }
        )
    }

    func binding<Value>(
        for keyPath: KeyPath<State, MviEventRaw<Value>?>
    ) -> EventBinding<Value> {
        binding(for: keyPath, id: keyPath)
    }
}

extension ViewStore where State: Equatable {

    convenience init(store: AnyStore<Action, State>) {
        self.init(
            store: store,
            removeDuplicates: ==
        )
    }
}
