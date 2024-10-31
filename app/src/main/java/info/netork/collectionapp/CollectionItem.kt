package info.netork.collectionapp.data

data class CollectionItem(
    val id: Int = 0,
    val sn: String,
    val name: String,
    val date: String,
    val voucherCode: String,
    val amount: Double
)