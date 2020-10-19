import SwiftUI

struct WithViewStore<Action, Result, State, Content>: View
    where Action: AnyObject, Result: AnyObject, State: AnyObject, Content: View
{

    @ObservedObject private var viewStore: ViewStore<Action, Result, State>

    private let content: (ViewStore<Action, Result, State>) -> Content

    init(
        _ store: Store<Action, Result, State>,
        removeDupicates isDuplicate: @escaping (State, State) -> Bool,
        @ViewBuilder content: @escaping (ViewStore<Action, Result, State>) -> Content
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
        @ViewBuilder content: @escaping (ViewStore<Action, Result, State>) -> Content
    ) {
        self.init(
            store,
            removeDupicates: ==,
            content: content
        )
    }
}
