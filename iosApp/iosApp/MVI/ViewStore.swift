import SwiftUI
import Combine
import shared

@dynamicMemberLookup
final class ViewStore<Action, State>: ObservableObject where Action: AnyObject, State: AnyObject {

    private(set) var state: State {
        willSet {
            objectWillChange.send()
        }
    }

    private let _dispatch: (@escaping (State) -> Action?) -> Void

    private let dispose: () -> Void

    private var viewCancellable: AnyCancellable?

    private var bindings: [AnyHashable: String] = [:]

    init(
        store: Store<Action, State>,
        removeDuplicates isDuplicate: @escaping (State, State) -> Bool
    ) {
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

    func binding<Value>(
        for keyPath: KeyPath<State, MviEvent<Value>?>,
        id: AnyHashable,
        _ file: StaticString = #file,
        _ line: UInt = #line
    ) -> EventBinding<Value> {
        let binding = state[keyPath: keyPath].map { event -> EventBinding<Value> in
            .init(
                get: { [unowned self] in
                    self.bindings[id] == event.id ? nil : event
                },
                set: { [unowned self] in
                    guard $0 == nil else { return }
                    guard self.bindings[id] != event.id else {
                        assertionFailure(
                            "Event with id \(event.id) is already consumed",
                            file: file,
                            line: line
                        )
                        return
                    }

                    self.bindings[id] = event.id
                }
            )
        }

        return binding ?? .constant(nil)
    }

    func binding<Value>(
        for keyPath: KeyPath<State, MviEvent<Value>?>,
        _ file: StaticString = #file,
        _ line: UInt = #line
    ) -> EventBinding<Value> {
        binding(for: keyPath, id: keyPath, file, line)
    }
}

extension ViewStore where State: Equatable {

    convenience init(store: Store<Action, State>) {
        self.init(
            store: store,
            removeDuplicates: ==
        )
    }
}
