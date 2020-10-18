import SwiftUI

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
