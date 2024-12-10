package site.sayaz.ofindex

import android.app.Application

class MainApp : Application(){
    override fun onCreate() {
        super.onCreate()
        MainActivity()
    }
}
