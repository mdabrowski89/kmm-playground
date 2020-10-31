import SwiftUI
import Combine
import shared

struct HomeView: View {

    typealias Store = AnyStore<HomeAction, HomeViewState>

    private let store: Store

    @State private var text: String = ""

    init(store: Store = .store(from: HomeViewModel())) {
        self.store = store
    }

    var body: some View {
        WithViewStore(store) { viewStore in
            ZStack {
                VStack {
                    HStack {
                        TextField("Add new task", text: $text)
                        Button {
                            viewStore.accept { $0.addTask(taskContent: text) }
                        } label: {
                            Text("Add")
                        }
                    }
                    .padding()
                    Divider()
                    Button("Delete completed Tasks") {
                        viewStore.accept { $0.deleteCompletedTasks() }
                    }
                    Divider()
                    List(viewStore.tasks ?? []) { task in
                        Button {
                            viewStore.accept { $0.updateTask(taskId: task.id, isDone: !task.isDone) }
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
                viewStore.accept { $0.loadDataIfNeeded() }
            }
            .alert(
                event: viewStore.binding(for: \.errorEvent),
                content: { error in
                    Alert(title: Text(error.message ?? ""))
                }
            )
            .onEvent(viewStore.taskAddedEvent) { _ in
                text.removeAll()
            }
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
            HomeView(store: .preview(state: .preview()))
        }
        .navigationViewStyle(StackNavigationViewStyle())
    }
}
#endif
