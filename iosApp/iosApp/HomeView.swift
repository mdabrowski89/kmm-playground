import SwiftUI
import Combine
import shared

typealias HomeStore = MviController<HomeAction, HomeResult, HomeViewState>

struct HomeView: View {

    private let store: HomeStore

    @State private var text: String = ""

    init(store: HomeStore = koin.get()) {
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
                            viewStore.accept { $0.updateTask(taskId: 0, isDone: !task.isDone) }
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
                isPresented: viewStore.binding(
                    get: { $0.error != nil },
                    send: HomeResult.EventConsumption.EventConsumptionErrorConsumed()
                ),
                content: {
                    viewStore.error.map { Alert(title: Text($0.message ?? "")) } ?? Alert(title: Text(""))
                }
            )
        }
    }
}

//struct ContentView_Previews: PreviewProvider {
//    static var previews: some View {
//        ContentView()
//    }
//}
