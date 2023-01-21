package be.ehb.finalworkjonathandewit.LiveVideo.SRT

import android.net.Uri
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.upstream.BaseDataSource
import com.google.android.exoplayer2.upstream.DataSpec
import io.github.thibaultbee.srtdroid.enums.SockOpt
import io.github.thibaultbee.srtdroid.enums.Transtype
import io.github.thibaultbee.srtdroid.models.Socket
import java.io.IOException
import java.util.*


//This code is largely inspired by the response of YoussefHenna on the ExoPlayer issue #8647
//titled 'Secure Reliable Transport (SRT) support'
//https://github.com/google/ExoPlayer/issues/8647


const val PAYLOAD_SIZE = 1316

class SrtLiveStreamDataSource(private val srtUrl: String, private val port: Int, private val passPhrase: String?) :

    BaseDataSource(/*isNetwork*/true) {

    private var socket: Socket? = null
    private val byteQueue: Queue<Byte> = LinkedList()


    override fun open(dataSpec: DataSpec): Long {
        socket = Socket()
        socket?.setSockFlag(SockOpt.TRANSTYPE, Transtype.LIVE)
        socket?.setSockFlag(SockOpt.PAYLOADSIZE, PAYLOAD_SIZE)
        if(passPhrase != null){
            socket?.setSockFlag(SockOpt.PASSPHRASE, passPhrase)
        }
        socket?.connect(srtUrl, port)
        return C.LENGTH_UNSET.toLong()
    }


    /**
     * Receives from SRT socket and feeds into a queue. Depending on the length requested
     * from exoplayer, that amount of bytes is polled from queue and onto the buffer with the given offset.
     *
     * You cannot directly receive at the given length from the socket, because SRT uses a
     * predetermined payload size that cannot be dynamic
     */
    override fun read(buffer: ByteArray, offset: Int, length: Int): Int {
        if (length == 0) {
            return 0
        }
        var bytesReceived = 0
        if (socket != null) {
            val received = socket!!.recv(PAYLOAD_SIZE)
            for (byte in received.second /*received byte array*/) {
                byteQueue.offer(byte)
            }
            repeat(length) { index ->
                val byte = byteQueue.poll()
                if (byte != null) {
                    buffer[index + offset] = byte
                    bytesReceived++
                }
            }
            return bytesReceived
        }
        throw IOException("Couldn't read bytes at offset: $offset")
    }

    override fun getUri(): Uri? {
        return Uri.parse("srt://$srtUrl:$port")
    }

    override fun close() {
        socket?.close()
        socket = null
    }
}