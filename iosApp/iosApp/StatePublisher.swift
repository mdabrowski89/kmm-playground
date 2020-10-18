import Combine
import shared

struct StatePublisher<Output>: Publisher where Output: AnyObject {
    typealias Failure = Never

    typealias Handler = (@escaping (Output?) -> Void) -> Closeable

    private let stateHandler: Handler

    init(_ commonFlow: CommonFlow<Output>) {
        self.stateHandler = commonFlow.watch
    }

    func receive<S>(subscriber: S) where S: Subscriber, Failure == S.Failure, Output == S.Input {
        let subscription = Subscription(stateHandler: stateHandler, downstream: subscriber)
        subscriber.receive(subscription: subscription)
    }
}

private extension StatePublisher {

  final class Subscription<Downstream: Subscriber> where Downstream.Input == Output {
        private var downstream: Downstream?
        private let closeable: Closeable

        init(stateHandler: @escaping Handler, downstream: Downstream) {
            self.downstream = downstream
            self.closeable = stateHandler { state in
                if let state = state {
                    _ = downstream.receive(state)
                }
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
        closeable.close()
        downstream = nil
    }
}
