package pl.mobite.playground.ui.base.viewbinding

import androidx.viewbinding.ViewBinding

/**
 * Method that will be used to create [ViewBinding].
 */
enum class CreateMethod {
    /**
     * Use `ViewBinding.bind(View)`
     */
    BIND,

    /**
     * Use `ViewBinding.inflate(LayoutInflater)`
     */
    INFLATE
}
