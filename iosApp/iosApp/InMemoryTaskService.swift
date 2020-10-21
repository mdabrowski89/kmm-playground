import Foundation
import shared

extension Task: Identifiable {
    // no-op
}

final class InMemoryTaskService: TaskService {

    private var tasks: [Task] = []

    func count(completionHandler: @escaping (KotlinInt?, Error?) -> Void) {
        completionHandler(
            KotlinInt(integerLiteral: tasks.count),
            nil
        );
    }
    
    func delete(tasks: [Task], completionHandler: @escaping (KotlinUnit?, Error?) -> Void) {
        completionHandler(KotlinUnit(), nil)
    }
    
    func deleteAll(completionHandler: @escaping (KotlinUnit?, Error?) -> Void) {
        tasks.removeAll()
        completionHandler(KotlinUnit(), nil)
    }
    
    func getAll(completionHandler: @escaping ([Task]?, Error?) -> Void) {
        completionHandler(tasks, nil)
    }
    
    func getAllDone(completionHandler: @escaping ([Task]?, Error?) -> Void) {
        completionHandler(tasks.filter { $0.isDone }, nil)
    }
    
    func getForId(id: Int64, completionHandler: @escaping ([Task]?, Error?) -> Void) {
        completionHandler(tasks.filter { $0.id == id }, nil)
    }
    
    func insert(task: Task, completionHandler: @escaping (KotlinLong?, Error?) -> Void) {
        tasks.insert(task, at: 0)
        completionHandler(KotlinLong(value: task.id), nil)
    }
    
    func update(task: Task, completionHandler: @escaping (KotlinUnit?, Error?) -> Void) {
        if let index = tasks.firstIndex(where: { $0.id == task.id }) {
            tasks[index] = task
        }

        completionHandler(KotlinUnit(), nil)
    }
}
