import SwiftUI
import class shared.MviEvent

extension View {

    func onEvent<Value>(
        _ event: MviEvent<Value>?,
        perform action: @escaping (Event<Value>) -> Void
    ) -> some View {
        onChange(of: event) {
            $0.map(Event.init).map(action)
        }
    }
}

struct EventAlertViewModifier<Value>: ViewModifier where Value: AnyObject {

    @State private var alertEvent: Event<Value>?

    let event: MviEvent<Value>?

    let content: (Value) -> Alert

    func body(content: Content) -> some View {
        content
            .onEvent(event) { event in
                alertEvent = event
            }
            .alert(item: $alertEvent) { event in
                self.content(event.value)
            }
    }
}

extension View {

    func alert<Value>(
        event: MviEvent<Value>?,
        content: @escaping (Value) -> Alert
    ) -> some View {
        modifier(
            EventAlertViewModifier(
                event: event,
                content: content
            )
        )
    }
}
