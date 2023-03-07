package be.ehb.finalworkjonathandewit.Fragments

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import be.ehb.finalworkjonathandewit.Activitys.MainActivity
import be.ehb.finalworkjonathandewit.LiveVideo.SRT.SrtLiveStreamDataSourceFactory
import be.ehb.finalworkjonathandewit.R
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.StyledPlayerView

class LiveVideoFragment : Fragment(R.layout.fragment_livevideo){


    private lateinit var player: ExoPlayer
    private var srtServerIp: String? = null
    private var outgoingStreamPort: Int? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        //This code is inspired by the response of YoussefHenna on the ExoPlayer issue #8647
        //titled 'Secure Reliable Transport (SRT) support'
        //https://github.com/google/ExoPlayer/issues/8647

        var playerView = view.findViewById<StyledPlayerView>(R.id.exoPlayerView)




        arguments?.let {
            srtServerIp = it.getString("ip")
            outgoingStreamPort = it.getInt("port")
        }

        val url = srtServerIp.toString()
        val port = outgoingStreamPort!!

        val source = ProgressiveMediaSource.Factory(SrtLiveStreamDataSourceFactory(url,port,),
        ).createMediaSource(MediaItem.fromUri(Uri.EMPTY))


        player = ExoPlayer.Builder(view.context)
            .build()
        player.setMediaSource(source)
        playerView.player = player


        player.prepare()
        player.play()
        player.playWhenReady = true

    }


    override fun onDestroyView() {
        super.onDestroyView()
        player.release()

        (activity as MainActivity?)?.enableNavBar()
    }
}