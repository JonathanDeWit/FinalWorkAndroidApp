package be.ehb.finalworkjonathandewit.Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class User (
    @PrimaryKey
    @ColumnInfo(name = "user_name") var userName:String,
    var email:String,
    @ColumnInfo(name = "first_name") var firstName:String,
    @ColumnInfo(name = "last_name") var lastName: String,
    @ColumnInfo(name = "last_update") var lastUpdate: String

){

}