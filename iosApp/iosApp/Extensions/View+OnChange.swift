import SwiftUI

fileprivate struct OnChangeViewModifier<Value>: ViewModifier where Value: Equatable {

    @State var initialValue: Value

    let value: Value

    let action: (Value) -> Void

    func body(content: Content) -> some View {
        if initialValue != value {
            DispatchQueue.main.async {
                initialValue = value
                action(value)
            }
        }

        return content.onAppear()
    }
}

extension View {

    @_disfavoredOverload
    @ViewBuilder
    func onChange<Value>(
        of value: Value,
        perform action: @escaping (Value) -> Void
    ) -> some View where Value: Equatable {
        if #available(iOS 14.0, *) {
            onChange(of: value, perform: action)
        } else {
            modifier(
                OnChangeViewModifier(
                    initialValue: value,
                    value: value,
                    action: action
                )
            )
        }
    }
}
