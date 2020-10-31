import struct SwiftUI.Binding
import class shared.MviEvent

typealias EventBinding<Value> = Binding<MviEvent<Value>?> where Value: AnyObject
