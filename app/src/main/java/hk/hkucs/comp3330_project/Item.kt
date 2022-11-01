package hk.hkucs.comp3330_project

class Item(itemName: String?, notes: String?, category: String?, expiryDate: String?, reminder: String?) {
    var itemName: String
    var notes: String
    var category: String
    var expiryDate: String
    var reminder: String

    init{
        this.itemName = itemName ?: ""
        this.notes = notes ?: ""
        this.category = category ?: ""
        this.expiryDate = expiryDate ?: ""
        this.reminder = reminder ?: ""
    }



}