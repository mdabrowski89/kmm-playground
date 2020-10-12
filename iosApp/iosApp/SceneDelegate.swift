import UIKit
import SwiftUI
import shared

typealias CoroutineScope = Kotlinx_coroutines_coreCoroutineScope
typealias CoroutineScopeWrapper = ViewModelIOS

final class ViewStore<Action, Result, State>: ObservableObject
    where Action: AnyObject, Result: AnyObject, State: AnyObject
{

    typealias Store = MviController<Action, Result, State>

    let _accept: (@escaping (State) -> Action?) -> Void

    private let scope = CoroutineScopeWrapper()

    private(set) var state: State {
        willSet {
            objectWillChange.send()
        }
    }

    init(
        storeFactory: StoreFactory<Store>,
        removeDupicates isDuplicated: @escaping (State, State) -> Bool
    ) {
        let store = storeFactory(scope.viewModelScope)
        self._accept = store.accept
        self.state = store.defaultViewState()

        store.viewStatesFlow.watch { [weak self] state in
            guard let self = self else { return }

            print("Thread.current.isMainThread", Thread.current.isMainThread)

            if let state = state, !isDuplicated(self.state, state) {
                self.state = state
            }

            // print(self.state)
        }
    }

    deinit {
        scope.onCleared()
    }

    func accept(_ intent: @escaping (State) -> Action?) {
        _accept(intent)
    }
}

extension ViewStore where State: Equatable {

    convenience init(storeFactory: StoreFactory<Store>) {
        self.init(
            storeFactory: storeFactory,
            removeDupicates: ==
        )
    }
}

struct WithViewStore<Action, Result, State, Content>: View
    where Action: AnyObject, Result: AnyObject, State: AnyObject, Content: View
{

    typealias Store = MviController<Action, Result, State>

    typealias _ViewStore = ViewStore<Action, Result, State>

    let content: (_ViewStore) -> Content

    @ObservedObject private var viewStore: _ViewStore

    init(
        _ storeFactory: (CoroutineScope) -> Store,
        removeDupicates isDuplicated: @escaping (State, State) -> Bool,
        @ViewBuilder content: @escaping (_ViewStore) -> Content
    ) {
        self.viewStore = .init(storeFactory: storeFactory, removeDupicates: isDuplicated)
        self.content = content
    }

    var body: some View {
        content(viewStore)
    }
}

extension WithViewStore where State: Equatable {

    init(
        _ storeFactory: (CoroutineScope) -> Store,
        @ViewBuilder content: @escaping (_ViewStore) -> Content
    ) {
        self.init(
            storeFactory,
            removeDupicates: ==,
            content: content
        )
    }
}

extension Koin_coreKoin {

    func get<T: AnyObject>(_ type: T.Type, parameter: Any) -> T {
        return self.get(objCClass: T.self, parameter: parameter) as! T
    }

    func store<Store: AnyObject>(_ type: Store.Type = Store.self) -> (CoroutineScope) -> Store {
        return { scope in
            self.get(Store.self, parameter: scope)
        }
    }
}


class SceneDelegate: UIResponder, UIWindowSceneDelegate {

    var window: UIWindow?
    
    // lazy var homeViewModel = HomeViewModel()
    // lazy var homeMviController = homeViewModel.homeMviController
    
    func scene(_ scene: UIScene, willConnectTo session: UISceneSession, options connectionOptions: UIScene.ConnectionOptions) {
        // Use this method to optionally configure and attach the UIWindow `window` to the provided UIWindowScene `scene`.
        // If using a storyboard, the `window` property will automatically be initialized and attached to the scene.
        // This delegate does not imply the connecting scene or session are new (see `application:configurationForConnectingSceneSession` instead).

        // Create the SwiftUI view that provides the window contents.
        
        // TODO: move connect with UI
        // homeMviController.viewStatesFlow.watch { viewState in
        //     let inProgress = viewState?.inProgress
        //     let tasks = viewState?.tasks
        //     let newTasksAdded = viewState?.newTaskAdded
        //     let error = viewState?.error
        // }
        
        let contentView = NavigationView {
            ContentView()
        }
        .navigationViewStyle(StackNavigationViewStyle())

        // Use a UIHostingController as window root view controller.
        if let windowScene = scene as? UIWindowScene {
            let window = UIWindow(windowScene: windowScene)
            window.rootViewController = UIHostingController(rootView: contentView)
            self.window = window
            window.makeKeyAndVisible()
        }
        
        // TODO: connect with UI
        // homeMviController.accept { viewState in
        //     return viewState.loadDataIfNeeded()
        // }
    }

    func sceneDidDisconnect(_ scene: UIScene) {
        // Called as the scene is being released by the system.
        // This occurs shortly after the scene enters the background, or when its session is discarded.
        // Release any resources associated with this scene that can be re-created the next time the scene connects.
        // The scene may re-connect later, as its session was not neccessarily discarded (see `application:didDiscardSceneSessions` instead).
        // homeViewModel.onCleared()
    }

    func sceneDidBecomeActive(_ scene: UIScene) {
        // Called when the scene has moved from an inactive state to an active state.
        // Use this method to restart any tasks that were paused (or not yet started) when the scene was inactive.
    }

    func sceneWillResignActive(_ scene: UIScene) {
        // Called when the scene will move from an active state to an inactive state.
        // This may occur due to temporary interruptions (ex. an incoming phone call).
    }

    func sceneWillEnterForeground(_ scene: UIScene) {
        // Called as the scene transitions from the background to the foreground.
        // Use this method to undo the changes made on entering the background.
    }

    func sceneDidEnterBackground(_ scene: UIScene) {
        // Called as the scene transitions from the foreground to the background.
        // Use this method to save data, release shared resources, and store enough scene-specific state information
        // to restore the scene back to its current state.
    }


}

