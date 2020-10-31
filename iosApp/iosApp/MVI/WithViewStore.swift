import SwiftUI

struct WithViewStore<Action, State, Content>: View where Content: View {

    typealias _ViewStore = ViewStore<Action, State>

    let content: (_ViewStore) -> Content

    @ObservedObject private var viewStore: _ViewStore

    init(
        _ store: AnyStore<Action, State>,
        removeDuplicates isDuplicate: @escaping (State, State) -> Bool,
        @ViewBuilder content: @escaping (_ViewStore) -> Content
    ) {
        self.viewStore = .init(
            store: store,
            removeDuplicates: isDuplicate
        )

        self.content = content
    }

    var body: some View {
        content(viewStore)
    }
}

extension WithViewStore where State: Equatable {

    init(
        _ store: AnyStore<Action, State>,
        @ViewBuilder content: @escaping (_ViewStore) -> Content
    ) {
        self.init(
            store,
            removeDuplicates: ==,
            content: content
        )
    }
}
