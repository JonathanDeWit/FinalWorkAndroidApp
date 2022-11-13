package be.ehb.finalworkjonathandewit.ViewModels

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import be.ehb.finalworkjonathandewit.Models.User
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class ApplicationViewModels : ViewModel() {
    private var queue: RequestQueue? = null

    fun getQueue(context: FragmentActivity?): RequestQueue {
        if (queue == null && context != null){
            queue = Volley.newRequestQueue(context)
        }
        return queue!!
    }

}