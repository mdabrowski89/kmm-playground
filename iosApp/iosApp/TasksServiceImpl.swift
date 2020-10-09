//
//  TasksServiceImpl.swift
//  iosApp
//
//  Created by Mariusz Dabrowski on 07/10/2020.
//  Copyright © 2020 orgName. All rights reserved.
//

import Foundation
import shared

// TODO: add proper implementation
final class TaskServiceImpl: TaskService {
    
    func count(completionHandler: @escaping (KotlinInt?, Error?) -> Void) {
        completionHandler(5, nil);
    }
    
    func delete(tasks: [Task], completionHandler: @escaping (KotlinUnit?, Error?) -> Void) {
        completionHandler(KotlinUnit(), nil)
    }
    
    func deleteAll(completionHandler: @escaping (KotlinUnit?, Error?) -> Void) {
        completionHandler(KotlinUnit(), nil)
    }
    
    func getAll(completionHandler: @escaping ([Task]?, Error?) -> Void) {
        let task = Task(id: 1, content: "Sample task", isDone: false)
        completionHandler([task], nil)
    }
    
    func getAllDone(completionHandler: @escaping ([Task]?, Error?) -> Void) {
        let task = Task(id: 1, content: "Sample task", isDone: false)
        completionHandler([task], nil)
    }
    
    func getForId(id: Int64, completionHandler: @escaping ([Task]?, Error?) -> Void) {
        let task = Task(id: 1, content: "Sample task", isDone: false)
        completionHandler([task], nil)
    }
    
    func insert(task: Task, completionHandler: @escaping (KotlinLong?, Error?) -> Void) {
        completionHandler(1, nil)
    }
    
    func update(task: Task, completionHandler: @escaping (KotlinUnit?, Error?) -> Void) {
        completionHandler(KotlinUnit(), nil)
    }
}
