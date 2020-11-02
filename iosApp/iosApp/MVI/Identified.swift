import Foundation

@dynamicMemberLookup
struct Identified<ID, Value>: Identifiable where ID: Hashable {
    let id: ID
    let value: Value

    init(_ value: Value, id: ID) {
        self.id = id
        self.value = value
    }

    init(_ value: Value, id: KeyPath<Value, ID>) {
        self.init(value, id: value[keyPath: id])
    }

    subscript<LocalValue>(
        dynamicMember keyPath: KeyPath<Value, LocalValue>
    ) -> LocalValue {
        value[keyPath: keyPath]
    }
}

extension Identified where ID == UUID {

    init(_ value: Value) {
        self.init(value, id: .init())
    }
}
