import SwiftUI
import Combine
import shared

typealias CoroutineScope = CoroutineScopesKt
typealias CoroutineScopeType = Kotlinx_coroutines_coreCoroutineScope

typealias Store<Action, Result, State> = MviController<Action, Result, State>
    where Action: AnyObject, Result: AnyObject, State: AnyObject

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

    private var cancellable: AnyCancellable?

    init(state: State) {
        self.state = state
        self.acceptAction = { _ in }
        self.acceptResult = { _ in }
    }

    init(
        store: Store<Action, Result, State>,
        removeDupicates isDuplicate: @escaping (State, State) -> Bool
    ) {
        self.state = store.defaultViewState()
        self.acceptAction = store.accept
        self.acceptResult = store.accept
        self.cancellable = StatePublisher(store.viewStatesFlow)
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

struct WithViewStore<Action, Result, State, Content>: View
    where Action: AnyObject, Result: AnyObject, State: AnyObject, Content: View
{

    typealias _ViewStore = ViewStore<Action, Result, State>

    let content: (_ViewStore) -> Content

    @ObservedObject private var viewStore: _ViewStore

    init(
        _ store: Store<Action, Result, State>,
        removeDupicates isDuplicate: @escaping (State, State) -> Bool,
        @ViewBuilder content: @escaping (_ViewStore) -> Content
    ) {
        self.viewStore = .init(
            store: store,
            removeDupicates: isDuplicate
        )

        self.content = content
    }

    var body: some View {
        content(viewStore)
    }
}

extension WithViewStore where State: Equatable {

    init(
        _ store: Store<Action, Result, State>,
        @ViewBuilder content: @escaping (_ViewStore) -> Content
    ) {
        self.init(
            store,
            removeDupicates: ==,
            content: content
        )
    }
}
