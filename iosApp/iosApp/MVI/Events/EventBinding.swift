import struct SwiftUI.Binding
import class shared.MviEventRaw

typealias EventBinding<Value> = Binding<MviEventRaw<Value>?> where Value: AnyObject
