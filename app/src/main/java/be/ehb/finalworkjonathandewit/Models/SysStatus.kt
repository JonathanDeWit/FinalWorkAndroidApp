package be.ehb.finalworkjonathandewit.Models

class SysStatus (
        var SysState: Boolean = false,
        var DeviceHasUser: Boolean = false,
        var Hubs: List<Hub> = emptyList(),
        var Cameras: List<Camera> = emptyList()
) {
        class Hub (
                var DeviceName: String? = null,
                var DeviceLocation: String? = null,
                var Id: Int? = null
        )

        class Camera (
                var LocalIp: String? = null,
                var DeviceName: String? = null,
                var DeviceLocation: String? = null,
                var Id: Int? = null,
                var TransmitVideoStream: Boolean = false,
                var HubReceiveVideoStream: Boolean = false,
                var VideoStream: VideoStreamOb? = null
        ) {
                class VideoStreamOb (
                        var Id: String? = null,
                        var SrtServerUserId: Int? = null,
                        var SrtServerIp: String = "",
                        var IncomingStreamPort: Int? = null,
                        var OutgoingStreamPort: Int = 0
                )
        }
}