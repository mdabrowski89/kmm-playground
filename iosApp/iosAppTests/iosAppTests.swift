import XCTest
import shared
import SnapshotTesting
import SwiftUI
@testable import iosApp

class iosAppTests: XCTestCase {

    override func setUp() {
        // Put setup code here. This method is called before the invocation of each test method in the class.
    }

    override func tearDown() {
        // Put teardown code here. This method is called after the invocation of each test method in the class.
    }

    func testExample() {
        let viewState = HomeViewState(
            inProgress: true,
            tasks: [
                .init(id: 0, content: "Task #1", isDone: false),
                .init(id: 1, content: "Task #2", isDone: true)
                
            ],
            newTaskAdded: nil,
            error: nil
        )

        let view = NavigationView {
            ContentView(
                store: TestStore(viewState: viewState)
            )
        }
        .navigationViewStyle(StackNavigationViewStyle())

        assertSnapshot(
            matching: view,
            as: .image(
                layout: .device(config: .iPhoneXr)
            )
        )
    }
}
