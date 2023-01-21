package be.ehb.finalworkjonathandewit.LiveVideo.SRT

import com.google.android.exoplayer2.upstream.DataSource


//This code is largely inspired by the response of YoussefHenna on the ExoPlayer issue #8647
//titled 'Secure Reliable Transport (SRT) support'
//https://github.com/google/ExoPlayer/issues/8647


class SrtLiveStreamDataSourceFactory(private val srtUrl: String,
                                    private val port: Int,
                                    private val passPhrase: String? = null)
        : DataSource.Factory {

        override fun createDataSource(): DataSource {
        return SrtLiveStreamDataSource(srtUrl, port, passPhrase)

    }



}