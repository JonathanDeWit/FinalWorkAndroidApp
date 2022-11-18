package be.ehb.finalworkjonathandewit.Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity()
class User (
    @PrimaryKey
    var Id:String="",
    @ColumnInfo(name = "user_name") var UserName:String="",
    @ColumnInfo(name = "email") var Email:String="",
    @ColumnInfo(name = "first_name") var FirstName:String="",
    @ColumnInfo(name = "last_name") var LastName: String="",
    @ColumnInfo(name = "last_update") var lastUpdate: String="",
    @ColumnInfo(name = "api_key") var apiKey: String="",
    @ColumnInfo(name = "api_key_date") var apiKeyDate: Date= Date()
){


    companion object{
        fun isTokenStilValide(apiKeyDate:Date):Boolean{

            val apikeyEndDate = Date(apiKeyDate.time+(14*60*1000))
            val now = Date()

            //inspired by https://www.techiedelight.com/compare-two-dates-in-kotlin/
            val cmp = apikeyEndDate.compareTo(now)
            when{
                cmp > 0 -> {
                    return true
                }
                cmp < 0 -> {
                    return false
                }
                else ->{
                    return false
                }
            }
        }
    }

}