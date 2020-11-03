import SwiftUI
import Combine
import shared

struct HomeView: View {

    @ObservedObject private var viewStore: ViewStore<HomeAction, HomeViewState>

    @State private var text: String = ""

    init(store: Store<HomeAction, HomeViewState> = HomeStore()) {
        self.viewStore = .init(store: store)
    }

    var body: some View {
        ZStack {
            VStack {
                HStack {
                    TextField("Add new task", text: $text)
                    Button {
                        viewStore.dispatch { $0.addTask(taskContent: text) }
                    } label: {
                        Text("Add")
                    }
                }
                .padding()
                Divider()
                Button("Delete completed Tasks") {
                    viewStore.dispatch { $0.deleteCompletedTasks() }
                }
                Divider()
                List(viewStore.tasks ?? []) { task in
                    Button {
                        viewStore.dispatch { $0.updateTask(taskId: task.id, isDone: !task.isDone) }
                    } label: {
                        HStack {
                            Text("\(task.content)")
                            Spacer()
                            Image(systemName: task.isDone ? "checkmark.circle.fill" : "circle")
                        }
                    }
                }
            }
        }
        .navigationBarTitle("Tasks", displayMode: .inline)
        .navigationBarItems(
            leading: viewStore.inProgress ? AnyView(ActivityIndicator()) : AnyView(EmptyView())
        )
        .onAppear {
            viewStore.dispatch { $0.loadDataIfNeeded() }
        }
        .onEvent(viewStore.taskAddedEvent) { _ in
            text.removeAll()
        }
        .alert(event: viewStore.errorEvent) { error in
            Alert(title: Text(error.message ?? ""))
        }
    }
}

#if DEBUG
extension HomeViewState {

    static func preview() -> HomeViewState {
        .init(
            inProgress: true,
            tasks: nil,
            taskAddedEvent: nil,
            errorEvent: nil
        )
    }
}

struct HomeView_Previews: PreviewProvider {

    static var previews: some View {
        NavigationView {
            HomeView(store: EmptyStore(initialState: .preview()))
        }
        .navigationViewStyle(StackNavigationViewStyle())
    }
}
#endif
