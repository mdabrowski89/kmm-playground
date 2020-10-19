import SwiftUI
import Combine
import shared

typealias CoroutineScope = CoroutineScopeIOS
typealias CoroutineScopeType = Kotlinx_coroutines_coreCoroutineScope

extension CoroutineScope {

    static let main: CoroutineScope = CoroutineScopesKt.mainScope()
}

final class Store {
    
}

@dynamicMemberLookup
final class ViewStore<Action, Result, State>: ObservableObject
    where Action: AnyObject, Result: AnyObject, State: AnyObject
{

    typealias _Store = MviController<Action, Result, State>

    private(set) var state: State {
        willSet {
            objectWillChange.send()
        }
    }

    private let scope: CoroutineScope

    private let _acceptAction: (@escaping (State) -> Action?) -> Void

    private let _acceptResult: (Result) -> Void

    init(
        storeFactory: StoreFactory<_Store>,
        on scope: CoroutineScope = .main,
        removeDupicates isDuplicate: @escaping (State, State) -> Bool
    ) {
        let store = storeFactory(scope.scope)
        self._acceptAction = store.accept
        self._acceptResult = store.accept
        self.state = store.defaultViewState()
        self.scope = scope

        store.viewStatesFlow.watch { [weak self] state in
            guard let self = self else { return }

            if let state = state, !isDuplicate(self.state, state) {
                self.state = state
                print(state)
            }
        }
    }

    deinit {
        scope.cancel()
    }

    subscript<LocalState>(dynamicMember keyPath: KeyPath<State, LocalState>) -> LocalState {
      state[keyPath: keyPath]
    }

    func accept(_ intent: @escaping (State) -> Action?) {
        _acceptAction(intent)
    }

    func binding<LocalState>(
        get: @escaping (State) -> LocalState,
        send result: Result
    ) -> Binding<LocalState> {
        return .init(
            get: { get(self.state) },
            set: { _ in
                self._acceptResult(result)
            }
        )
    }
}

extension ViewStore where State: Equatable {

    convenience init(storeFactory: StoreFactory<_Store>) {
        self.init(
            storeFactory: storeFactory,
            removeDupicates: ==
        )
    }
}

struct WithViewStore<Action, Result, State, Content>: View
    where Action: AnyObject, Result: AnyObject, State: AnyObject, Content: View
{

    typealias _Store = MviController<Action, Result, State>

    typealias _ViewStore = ViewStore<Action, Result, State>

    let content: (_ViewStore) -> Content

    @ObservedObject private var viewStore: _ViewStore

    init(
        _ storeFactory: StoreFactory<_Store>,
        removeDupicates isDuplicate: @escaping (State, State) -> Bool,
        @ViewBuilder content: @escaping (_ViewStore) -> Content
    ) {
        self.viewStore = .init(
            storeFactory: storeFactory,
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
        _ storeFactory: StoreFactory<_Store>,
        @ViewBuilder content: @escaping (_ViewStore) -> Content
    ) {
        self.init(
            storeFactory,
            removeDupicates: ==,
            content: content
        )
    }
}
