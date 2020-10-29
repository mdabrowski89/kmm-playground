# kmm-playground

Sample app written in oreder to demonstrate usage of custom MVI Coroutine base framework in Kotlin Mutliplatfrom Mobile project. 

All logic and processing is extracted to `shared` code. Native parts (Android and iOS) are only reposible for sending Actions (aka Intents) and rendering ViewState. They are also providing data service implementations.

>Note: It is not a final version of an MVI framework yet as work is still in progres.

App consist of one feature: simple TODO list which allows to track task statuses.
