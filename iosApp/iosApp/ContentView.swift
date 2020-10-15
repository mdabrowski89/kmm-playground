import SwiftUI
import Combine
import shared

typealias StoreFactory<Store> = (CoroutineScopeType) -> Store

struct ContentView: View {

    let storeFactory: StoreFactory<HomeMviController> = koin.store()

    @State var text: String = ""

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
            .alert(event: viewStore.error) { error in
                Alert(title: Text(error.message ?? ""))
            }
        }
    }
}

extension Task: Identifiable {
    // no-op
}

extension View {

    func alert<T>(event: shared.SingleEvent<T>?, content: (T) -> Alert) -> some View where T: AnyObject {
        alert(
            isPresented: .consume(event: event),
            content: {
                event?.argument.map(content) ?? Alert(title: Text(""))
            }
        )
    }
}

extension Binding where Value == Bool {

    static func consume<T>(event: SingleEvent<T>?) -> Self where T: AnyObject {
        guard let event = event, !event.isConsumed.value else {
            return .constant(false)
        }

        return .init(
            get: {
                return !event.isConsumed.value
            },
            set: {
                guard !$0 else { return }
                event.consume()
            }
        )
    }
}

struct ActivityIndicator: UIViewRepresentable {

    func makeUIView(context: Context) -> UIActivityIndicatorView {
        UIActivityIndicatorView()
    }
    
    func updateUIView(_ uiView: UIActivityIndicatorView, context: Context) {
        uiView.startAnimating()
    }
    
}

//struct ContentView_Previews: PreviewProvider {
//    static var previews: some View {
//        ContentView()
//    }
//}
