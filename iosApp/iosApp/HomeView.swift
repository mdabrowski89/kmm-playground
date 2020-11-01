import SwiftUI
import Combine
import shared

struct HomeView: View {
    
    typealias Store = AnyStore<HomeAction, HomeViewState>
    
    @ObservedObject private var viewStore: ViewStore<HomeAction, HomeViewState>
    
    @State private var text: String = ""

    @State private var error: Event<String>?

    init(store: Store = .store(from: HomeViewModel())) {
        self.viewStore = .init(store: store)
    }
    
    var body: some View {
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
        .alert(item: $error) { event in
            Alert(title: Text(event.value))
        }
        .onEvent(viewStore.taskAddedEvent) { _ in
            text.removeAll()
        }
        .onEvent(viewStore.errorEvent) { error in
            self.error = .init(error.message ?? "Error")
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
