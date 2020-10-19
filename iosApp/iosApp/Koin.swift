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
    let koinApplication = KoinIOSKt.doInitKoin(
        taskService: { _, _ in TaskServiceImpl() }
    )

    _koin = koinApplication.koin
}

private var _koin: Koin_coreKoin? = nil
var koin: Koin_coreKoin {
    return _koin!
}

extension Koin_coreKoin {

    func get<T: AnyObject>(_ type: T.Type = T.self) -> T {
        return self.get(objCClass: T.self) as! T
    }

    func get<T: AnyObject>(_ type: T.Type = T.self, parameter: Any) -> T {
        return self.get(objCClass: T.self, parameter: parameter) as! T
    }
}
