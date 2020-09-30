package pl.mobite.playground.ui.components

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import pl.mobite.playground.R

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navController = findNavController(supportFragmentManager.findFragmentByTag("NavHostFragment")!!)
    }

    override fun onSupportNavigateUp() = navController.navigateUp()
}
