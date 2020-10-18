import SwiftUI
import Combine
import shared

@dynamicMemberLookup
final class ViewStore<Action, Result, State>: ObservableObject
    where Action: AnyObject, Result: AnyObject, State: AnyObject
{

    private(set) var state: State {
        willSet {
            objectWillChange.send()
        }
    }

    private let acceptAction: (@escaping (State) -> Action?) -> Void

    private let acceptResult: (Result) -> Void

    private var viewCancellable: AnyCancellable?

    init(
        store: Store<Action, Result, State>,
        removeDupicates isDuplicate: @escaping (State, State) -> Bool
    ) {
        self.state = store.defaultViewState()
        self.acceptAction = store.accept
        self.acceptResult = store.accept
        self.viewCancellable = StatePublisher(store.viewStatesFlow.watch)
            .removeDuplicates(by: isDuplicate)
            .sink { [weak self] in self?.state = $0 }
    }

    subscript<LocalState>(dynamicMember keyPath: KeyPath<State, LocalState>) -> LocalState {
        state[keyPath: keyPath]
    }

    func accept(_ intent: @escaping (State) -> Action?) {
        acceptAction(intent)
    }

    func accept(_ result: Result) {
        acceptResult(result)
    }

    func binding<LocalState>(
        get: @escaping (State) -> LocalState,
        send result: Result
    ) -> Binding<LocalState> {
        return .init(
            get: { get(self.state) },
            set: { _ in
                self.accept(result)
            }
        )
    }
}

extension ViewStore where State: Equatable {

    convenience init(store: Store<Action, Result, State>) {
        self.init(
            store: store,
            removeDupicates: ==
        )
    }
}