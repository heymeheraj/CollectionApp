package info.netork.collectionapp

import android.app.Application
import android.util.Log

class CollectionApp : Application() {

    companion object {
        private const val TAG = "CollectionApp"
        private var instance: CollectionApp? = null

        fun getInstance(): CollectionApp {
            return instance ?: throw IllegalStateException("Application not initialized")
        }
    }

    override fun onCreate() {
        super.onCreate()
        try {
            instance = this
            initializeApp()
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing application", e)
        }
    }

    private fun initializeApp() {
        // Initialize any app-wide configurations here
        // For example: Database initialization, logging setup, etc.
    }

    override fun onTerminate() {
        super.onTerminate()
        try {
            // Clean up resources if needed
            instance = null
        } catch (e: Exception) {
            Log.e(TAG, "Error terminating application", e)
        }
    }
}