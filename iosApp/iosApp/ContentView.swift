import SwiftUI
import shared

import Combine

typealias StoreFactory<Store> = (CoroutineScopeType) -> Store

struct ContentView: View {

    let storeFactory: StoreFactory<HomeMviController> = koin.store()

    @State var text: String = ""

    @State var showAlert = false

    var body: some View {
        WithViewStore(storeFactory) { viewStore in
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
                        // self.showAlert.toggle()
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

                    viewStore.error.map {
                        SingleEventConsumer(event: $0) { error in
                            Text("Error: \(error.message ?? "")")
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
        }
        .alert(isPresented: $showAlert) {
            Alert(title: Text("Test"))
        }
    }
}

extension Task: Identifiable {
    // no-op
}

struct ActivityIndicator: UIViewRepresentable {

    func makeUIView(context: Context) -> UIActivityIndicatorView {
        UIActivityIndicatorView()
    }
    
    func updateUIView(_ uiView: UIActivityIndicatorView, context: Context) {
        uiView.startAnimating()
    }
    
}

final class SingleEvent<Value>: ObservableObject where Value: AnyObject {

    private(set) var value: Value? {
        willSet { objectWillChange.send() }
    }

    init(event: shared.SingleEvent<Value>) {
        event.consume { [weak self] in
            self?.value = $0
        }
    }
}

struct SingleEventConsumer<T, Content>: View where T: AnyObject, Content: View {

    @ObservedObject private(set) var event: SingleEvent<T>

    private let content: (T) -> Content

    @State private var value: T?

    init(
        event: shared.SingleEvent<T>,
        @ViewBuilder content: @escaping (T) -> Content
    ) {
        self.event = SingleEvent(event: event)
        self.content = content
    }

    var body: some View {
        event.value.map(content)
    }
}

//struct ContentView_Previews: PreviewProvider {
//    static var previews: some View {
//        ContentView()
//    }
//}
