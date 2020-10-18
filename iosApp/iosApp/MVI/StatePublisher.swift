import Combine
import shared

struct StatePublisher<Output>: Publisher where Output: AnyObject {
    typealias Failure = Never

    typealias Observer = (@escaping (Output?) -> Void) -> Closeable

    private let observer: Observer

    init(_ observer: @escaping Observer) {
        self.observer = observer
    }

    func receive<S>(subscriber: S) where S: Subscriber, Failure == S.Failure, Output == S.Input {
        let subscription = Subscription(observer: observer, downstream: subscriber)
        subscriber.receive(subscription: subscription)
    }
}

private extension StatePublisher {

  final class Subscription<Downstream: Subscriber> where Downstream.Input == Output {
        private var downstream: Downstream?
        private let closeable: Closeable

        init(observer: @escaping Observer, downstream: Downstream) {
            self.downstream = downstream
            self.closeable = observer {
                _ = $0.map(downstream.receive)
            }
        }
    }
}

extension StatePublisher.Subscription: Subscription {

    func request(_ demand: Subscribers.Demand) {
        // no-op
    }
}

extension StatePublisher.Subscription: Cancellable {

    func cancel() {
        print("StatePublisher.cancel")
        closeable.close()
        downstream = nil
    }
}
