import Combine

struct StatePublisher<Output>: Publisher {
    typealias Failure = Never

    typealias Observer = (@escaping (Output) -> Void) -> Void

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

        init(observer: @escaping Observer, downstream: Downstream) {
            self.downstream = downstream
            observer {
                _ = downstream.receive($0)
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
        downstream = nil
    }
}
