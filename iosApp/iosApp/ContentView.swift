import SwiftUI
import shared

typealias StoreFactory<Store> = (CoroutineScope) -> Store

struct ContentView: View {

    let storeFactory: StoreFactory<HomeMviController> = koin.store()

    var body: some View {
        WithViewStore(storeFactory) { viewStore in
                VStack {
                    Text("greet()" + "\(viewStore.state.inProgress)")
                        .onAppear {
                            // viewStore.accept { $0.loadDataIfNeeded() }
                            viewStore.accept { $0.updateTask(taskId: 1, isDone: true) }
                            viewStore.accept { $0.updateTask(taskId: 2, isDone: false) }
                        }

                    NavigationLink(
                        destination: ContentView(),
                        label: { Text("Test Next") }
                    )
                }
        }
    }
}

//struct ContentView_Previews: PreviewProvider {
//    static var previews: some View {
//        ContentView()
//    }
//}
