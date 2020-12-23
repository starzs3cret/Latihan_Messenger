package id.unlink.latihanmessenger.model

class MessagE(val map: MutableMap<String, Any>) {
    val fileUrl by map
    val id by map
    val imgUrl by map
    val name by map
    val photoUrl by map
    val text by map
    val time by map
    val user by map
}

class MessageDoc(val map: MutableMap<String, Any>) {
    val contact by map
    val id_chat by map
    val message by map
}
