import SwiftUI
import shared

typealias StoreFactory<Store> = (CoroutineScopeType) -> Store

struct ContentView: View {

    let storeFactory: StoreFactory<HomeMviController> = koin.store()

    var body: some View {
        WithViewStore(storeFactory) { viewStore in
            VStack {
                Text("loading in progress: " + "\(viewStore.state.inProgress)")
                List(viewStore.state.tasks ?? [], id: \.id) { task in
                    Text("" + "\(task.content)")
                }
            }
            .onAppear {
                viewStore.accept { $0.loadDataIfNeeded() }
            }
        }
    }
}

// extension Task: Identifiable {
// }

//struct ContentView_Previews: PreviewProvider {
//    static var previews: some View {
//        ContentView()
//    }
//}
