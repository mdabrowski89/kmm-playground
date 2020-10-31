import SwiftUI
import class shared.MviEvent

extension View {

    func onEvent<Value>(
        _ event: MviEvent<Value>?,
        perform action: @escaping (Value) -> Void
    ) -> some View {
        onChange(of: event) {
            $0.flatMap(\.value).map(action)
        }
    }
}

extension View {

    func alert<Value>(
        event: EventBinding<Value>,
        content: (Value) -> Alert
    ) -> some View {
        alert(item: event) { event in
            event.value.map(content) ?? Alert(title: Text(""))
        }
    }
}
