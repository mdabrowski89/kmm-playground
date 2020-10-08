//
//  HomeViewModel.swift
//  iosApp
//
//  Created by Mariusz Dabrowski on 08/10/2020.
//  Copyright © 2020 orgName. All rights reserved.
//

import Foundation
import shared

class HomeViewModel: ViewModelIOS {
    
    lazy var homeMviController: HomeMviController = koin.get(objCClass: HomeMviController.self, parameter: viewModelScope) as! HomeMviController;
    
}
