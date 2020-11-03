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

    private var prefix: String?

    private var viewCancellable: AnyCancellable?

    init(
        store: Store<Action, State>,
        removeDuplicates isDuplicate: @escaping (State, State) -> Bool
    ) where Action: AnyObject, State: AnyObject {
        self.state = store.initialState
        self._dispatch = store.dispatch
        self.dispose = store.dispose
        self.prefix = store.prefix
        self.viewCancellable = StatePublisher(store.stateObserver)
            .dropFirst()
            .removeDuplicates(by: isDuplicate)
            .sink { [weak self] state in
                #if DEBUG
                FreezerKt.freeze(obj: state)
                self?.debugPrint(
                    label: "receive state:",
                    object: state
                )
                #endif

                self?.state = state
            }
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

    #if DEBUG
    private func debugPrint(label: String, object: Any) {
        guard let prefix = prefix else { return }
        debugQueue.async {
            print(
                """
                \(prefix.isEmpty ? "" : "[\(prefix)] ")\(label)
                    \(object)

                """
            )
        }
    }
    #endif
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

#if DEBUG
private let debugQueue = DispatchQueue(
    label: "mvi.ViewStore.DebugQueue",
    qos: .background
)
#endif
