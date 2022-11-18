package hk.hkucs.comp3330_project

class Category(categoryName: String?, categorySize: Int?, imageId: Int?, categoryItems: ArrayList<Item>) {
    var categoryName: String
    var categorySize: Int
    var imageId: Int
    var categoryItems: ArrayList<Item>

    init{
        this.categoryName = categoryName ?: ""
        this.categorySize = categorySize ?: 0
        this.imageId = imageId ?: 0
        this.categoryItems = categoryItems
    }



}