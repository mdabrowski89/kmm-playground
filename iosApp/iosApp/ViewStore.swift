import SwiftUI
import Combine
import shared

typealias CoroutineScopeType = Kotlinx_coroutines_coreCoroutineScope

typealias CoroutineScope = CoroutineScopeKt

final class ViewStore<Action, Result, State>: ObservableObject
    where Action: AnyObject, Result: AnyObject, State: AnyObject
{

    typealias Store = MviController<Action, Result, State>

    let _accept: (@escaping (State) -> Action?) -> Void

    private let coroutineScope: CoroutineScopeType

    private(set) var state: State {
        willSet {
            objectWillChange.send()
        }
    }

    init(
        storeFactory: StoreFactory<Store>,
        on coroutineScope: CoroutineScopeType = CoroutineScope.mainScope(),
        removeDupicates isDuplicate: @escaping (State, State) -> Bool
    ) {
        let store = storeFactory(coroutineScope)
        self._accept = store.accept
        self.state = store.defaultViewState()
        self.coroutineScope = coroutineScope

        store.viewStatesFlow.watch { [weak self] state in
            guard let self = self else { return }

            print("Thread.current.isMainThread", Thread.current.isMainThread)

            if let state = state, !isDuplicate(self.state, state) {
                self.state = state
            }

            // print(self.state)
        }
    }

    deinit {
        CoroutineScope.cancel(scope: coroutineScope)
    }

    func accept(_ intent: @escaping (State) -> Action?) {
        _accept(intent)
    }
}

extension ViewStore where State: Equatable {

    convenience init(storeFactory: StoreFactory<Store>) {
        self.init(
            storeFactory: storeFactory,
            removeDupicates: ==
        )
    }
}

struct WithViewStore<Action, Result, State, Content>: View
    where Action: AnyObject, Result: AnyObject, State: AnyObject, Content: View
{

    typealias Store = MviController<Action, Result, State>

    typealias _ViewStore = ViewStore<Action, Result, State>

    let content: (_ViewStore) -> Content

    @ObservedObject private var viewStore: _ViewStore

    init(
        _ storeFactory: StoreFactory<Store>,
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
        _ storeFactory: StoreFactory<Store>,
        @ViewBuilder content: @escaping (_ViewStore) -> Content
    ) {
        self.init(
            storeFactory,
            removeDupicates: ==,
            content: content
        )
    }
}
