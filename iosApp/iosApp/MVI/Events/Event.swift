import struct Foundation.UUID
import class shared.MviEvent

typealias Event<Value> = Identified<UUID, Value>

extension Identified where ID == UUID, Value: AnyObject {

    init(_ event: MviEvent<Value>) {
        let uuid = UUID(uuidString: event.id) ?? .init()
        self.init(event.value, id: uuid)
    }
}
