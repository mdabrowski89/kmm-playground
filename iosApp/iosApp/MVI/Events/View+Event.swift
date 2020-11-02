import SwiftUI
import class shared.MviEvent

extension View {

    func onEvent<Value>(
        _ event: MviEvent<Value>?,
        perform action: @escaping (Value) -> Void
    ) -> some View {
        onChange(of: event) {
            $0.map(\.value).map(action)
        }
    }
}
