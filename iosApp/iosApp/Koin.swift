//
//  Koin.swift
//  iosApp
//
//  Created by Mariusz Dabrowski on 08/10/2020.
//  Copyright © 2020 orgName. All rights reserved.
//

import Foundation
import shared

func initKoin() {
    // TODO: could change into provider
    let taskService = TaskServiceImpl()
    
    let koinApplication = KoinIOSKt.doInitKoin(
        taskService: taskService
    )

    _koin = koinApplication.koin
}

private var _koin: Koin_coreKoin? = nil
var koin: Koin_coreKoin {
    return _koin!
}

extension Koin_coreKoin {

    func get<T: AnyObject>(_ type: T.Type = T.self) -> T {
        return get(objCClass: T.self) as! T
    }
}
