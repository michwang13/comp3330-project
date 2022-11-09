package hk.hkucs.comp3330_project

<<<<<<< HEAD
class Item(itemID: String?, itemName: String?, notes: String?, category: String?, expiryDate: String?, reminder: String?, imageURI: String?) {
    var itemID: String
    var itemName: String
    var notes: String
    var category: String
    var expiryDate: String
    var reminder: String
    var imageURI: String

    init{
        this.itemID = itemID ?: ""
        this.itemName = itemName ?: ""
        this.notes = notes ?: ""
        this.category = category ?: ""
        this.expiryDate = expiryDate ?: ""
        this.reminder = reminder ?: ""
        this.imageURI = imageURI ?: ""
    }
=======
data class Item(val itemName: String?, val notes: String?, val category: String?, val expiryDate: String?, val reminder: String?, val imageId: Int?) : java.io.Serializable {
//    var itemName: String
//    var notes: String
//    var category: String
//    var expiryDate: String
//    var reminder: String
//    var imageId: Int
//
//    init{
//        this.itemName = itemName ?: ""
//        this.notes = notes ?: ""
//        this.category = category ?: ""
//        this.expiryDate = expiryDate ?: ""
//        this.reminder = reminder ?: ""
//        this.imageId = imageId ?: 0
//    }
>>>>>>> ef33da1 (Categories Page Developed)



}