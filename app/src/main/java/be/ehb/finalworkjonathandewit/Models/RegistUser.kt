package be.ehb.finalworkjonathandewit.Models

class RegistUser(
    var Email:String,
    var FirstName:String,
    var LastName:String,
    var Password:String
) {
    fun isNotEmpty(): Boolean{
        return Email.isNotBlank() && FirstName.isNotBlank() && LastName.isNotBlank() && Password.isNotBlank()
    }
}