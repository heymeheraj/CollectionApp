package info.netork.collectionapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import info.netork.collectionapp.ui.theme.CollectionAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CollectionAppTheme {
                CollectionApp()
            }
        }
    }
}